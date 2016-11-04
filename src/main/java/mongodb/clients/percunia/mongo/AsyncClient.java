/**
 * Copyright (c) 2016 Abhishek Chawla <abhidtu@gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mongodb.clients.percunia.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mongodb.clients.percunia.Config;
import mongodb.clients.percunia.Ipersistence;
import mongodb.clients.percunia.ResultCallback;
import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Updates.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by linux on 30/5/16.
 */
public class AsyncClient implements Ipersistence {

    MongoClient mongoClient;

    MongoDatabase database;

    static final Logger logger = LoggerFactory.getLogger(AsyncClient.class);

    private static AsyncClient instance = new AsyncClient();

    private static ObjectMapper mapper = new ObjectMapper();

    String errorMessage = "no changes made";

    String successMessage = "";

    private AsyncClient() {

        logger.info("initiating connection to Mongo");

        try {

            ConnectionString uri = new ConnectionString("mongodb://"+ Config.USERNAME+":"+Config.PASSWORD+"@"+Config.HOST+"/?authSource="+Config.DB_NAME);
           //ConnectionString uri = new ConnectionString("mongodb://localhost");

            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(Config.DB_NAME);

        }catch (Exception e){
            logger.warn(e.toString());
        }

    }

    public static AsyncClient getInstance() {
        return instance;
    }

    @Override
    public void createDocument(final String collectionName, Object object, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        try {
            String json = mapper.writeValueAsString(object);

            Document doc = Document.parse(json);

            collection.insertOne(doc, new SingleResultCallback<Void>() {
                @Override
                public void onResult(final Void result, final Throwable t) {
                    onInsertResultAction(t,"success",callback);
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }

    }

    @Override
    public void removeDocument(final String collectionName, Criteria criteria, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.deleteOne(criteria.getRestrictions(), new SingleResultCallback<DeleteResult>() {
            @Override
            public void onResult(final DeleteResult result, final Throwable t) {
                onDeleteResultAction(result,t,"success",callback);
            }
        });

    }

    @Override
    public void removeDocumentField(final String collectionName, Criteria criteria, String field, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.updateOne(criteria.getRestrictions(), unset(field), new SingleResultCallback<UpdateResult>() {
            @Override
            public void onResult(final UpdateResult result, final Throwable t) {
                onUpdateResultAction(result,t,result.getModifiedCount() + " " + collectionName + " entity successfully updated",callback);
            }
        });

    }

    @Override
    public void updateDocumentField(final String collectionName, Criteria criteria, final String field, Object value, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.updateOne(criteria.getRestrictions(), set(field, value), new SingleResultCallback<UpdateResult>() {
            @Override
            public void onResult(final UpdateResult result, final Throwable t) {
                onUpdateResultAction(result,t,"successfully updated EntityObject field= "+ field+" Entity = " + collectionName,callback);
            }
        });
    }

    @Override
    public void updateDocumentFields(final String collectionName, Criteria criteria, HashMap<String, String> map, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        List<Bson> list = new ArrayList<>();

        for(final Map.Entry m : map.entrySet()){
            try {
                Document doc = Document.parse(m.getValue().toString());
                list.add(set(m.getKey().toString(), doc));
            }catch (Exception e){
                list.add(set(m.getKey().toString(), m.getValue().toString()));
            }
        }

        logger.info("updating EntityObject fields");
        collection.updateOne(criteria.getRestrictions(), combine(list), new SingleResultCallback<UpdateResult>() {
            @Override
            public void onResult(final UpdateResult result, final Throwable t) {
                onUpdateResultAction(result,t,"successfully updated EntityObject fields Entity = " + collectionName,callback);
            }
        });

    }


    @Override
    public void pushIntoDocumentField(final String collectionName, Criteria criteria, final String field, List<String> values, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        List<Document> docs = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for(String value : values) {
            try {
                docs.add(Document.parse(value));
            } catch (Exception e) {
                strings.add(value);
            }
        }


        if(!docs.isEmpty()) {

            collection.updateOne(criteria.getRestrictions(), addEachToSet(field, docs), new SingleResultCallback<UpdateResult>() {
                @Override
                public void onResult(final UpdateResult result, final Throwable t) {
                    onUpdateResultAction(result,t,"success",callback);
                }
            });

        }

        if(!strings.isEmpty()) {

            collection.updateOne(criteria.getRestrictions(), addEachToSet(field, strings), new SingleResultCallback<UpdateResult>() {
                @Override
                public void onResult(final UpdateResult result, final Throwable t) {
                    onUpdateResultAction(result,t,"success",callback);
                }
            });

        }

    }

    @Override
    public void pullSubDocumentsFromDocumentField(final String collectionName, Criteria criteria, final String field, final String subField, final List<String> subFieldValues, final ResultCallback callback) {
        Recurse(subFieldValues.size() - 1, collectionName, criteria, field, subField, subFieldValues, callback);
    }

    @Override
    public void updateSubDocumentsInDocumentField(final String collectionName, final Criteria criteria, final String field, String subField, List<String> subFieldValues, final List<String> newValues, final ResultCallback callback) {

        pullSubDocumentsFromDocumentField(collectionName, criteria, field, subField, subFieldValues, new ResultCallback() {
            @Override
            public void onSuccess(String message) {

                pushIntoDocumentField(collectionName, criteria, field, newValues, new ResultCallback() {
                    @Override
                    public void onSuccess(String message) {
                        callback.onSuccess("");
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError(message);
                    }
                });

            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });

    }

    @Override
    public void pullStringsFromDocumentField(final String collectionName, Criteria criteria, final String field, List<String> values, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.updateOne(criteria.getRestrictions(), pullAll(field, values), new SingleResultCallback<UpdateResult>() {
            @Override
            public void onResult(final UpdateResult result, final Throwable t) {
                onUpdateResultAction(result,t,"success",callback);
            }
        });

    }

    @Override
    public void getDocumentFieldValueViaFilter(final String collectionName, Criteria criteria, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.find(criteria.getRestrictions()).projection(criteria.getProjections()).first(new SingleResultCallback<Document>() {
            @Override
            public void onResult(final Document document, final Throwable t) {
                onDocumentResult(document,t,"success",callback);
            }
        });

    }

    @Override
    public void getDocumentViaFilter(final String collectionName, Criteria criteria, final ResultCallback callback) {

        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.find(criteria.getRestrictions()).first(new SingleResultCallback<Document>() {
            @Override
            public void onResult(final Document document, final Throwable t) {
                onDocumentResult(document,t,"success",callback);
            }
        });
    }


    //utility methods
    void Recurse(final int index, final String entityName, final Criteria criteria, final String field, final String subField, final List<String> subFieldValues, final ResultCallback callback) {

        if(index == -1){
            callback.onSuccess("");
            return;
        }

        MongoCollection<Document> collection = database.getCollection(entityName);

        collection.updateOne(criteria.getRestrictions(), pull(field, new Document(subField, subFieldValues.get(index))), new SingleResultCallback<UpdateResult>() {

            @Override
            public void onResult(final UpdateResult result, final Throwable t) {

                if (t == null) {
                    if (result.getModifiedCount() > 0) {

                        Recurse(index - 1, entityName, criteria, field, subField, subFieldValues, callback);

                    } else {
                        callback.onError(errorMessage);
                    }
                } else {
                    callback.onError(t.toString());
                    t.printStackTrace();
                }

            }

        });

    }

    private void onUpdateResultAction(UpdateResult result, Throwable t, String message, ResultCallback callback){
        if (t == null) {
            if (result.getModifiedCount() > 0) {
                logger.info(message);
                callback.onSuccess("");
            } else {
                callback.onError(errorMessage);
            }
        } else {
            callback.onError(t.toString());
        }
    }

    private void onDeleteResultAction(DeleteResult result, Throwable t, String message, ResultCallback callback){
        if (t == null) {
            if (result.getDeletedCount() > 0) {
                logger.info(message);
                callback.onSuccess("");
            } else {
                callback.onError(errorMessage);
            }
        } else {
            callback.onError(t.toString());
        }
    }

    private void onInsertResultAction(Throwable t, String message, ResultCallback callback){
        if(t == null) {
            logger.info(message);
            callback.onSuccess("");
        }else{
            callback.onError(t.toString());
        }
    }

    private void onDocumentResult(Document document, Throwable t, String message, ResultCallback callback) {
        if (t == null) {
            if (document != null) {
                logger.info(message);
                callback.onSuccess(document.toJson());
            } else {
                callback.onError("no document found matching query");
            }
        } else {
            callback.onError(t.toString());
        }
    }

}