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
package mongodb.clients.percunia;

import mongodb.clients.percunia.mongo.Criteria;

import java.util.HashMap;
import java.util.List;

/**
 * Created by linux on 30/5/16.
 */
public interface Ipersistence {

    void createDocument(String collectionName, Object object, ResultCallback callback);
    void removeDocument(String collectionName, Criteria criteria, ResultCallback callback);
    void removeDocumentField(String collectionName, Criteria criteria, String field, ResultCallback callback);
    void updateDocumentField(String collectionName, Criteria criteria, String field, Object value, ResultCallback callback);
    void updateDocumentFields(String collectionName, Criteria criteria, HashMap<String, String> map, ResultCallback callback);
    void pushIntoDocumentField(String collectionName, Criteria criteria, String field, List<String> values, ResultCallback callback);
    void pullSubDocumentsFromDocumentField(String collectionName, Criteria criteria, String field, String subField, List<String> subFieldValue, ResultCallback callback);
    void updateSubDocumentsInDocumentField(String collectionName, Criteria criteria, String field, String subField, List<String> subFieldValues, List<String> newValues, ResultCallback callback);
    void pullStringsFromDocumentField(String collectionName, Criteria criteria, String field, List<String> values, ResultCallback callback);
    void getDocumentFieldValueViaFilter(String collectionName, Criteria criteria, ResultCallback callback);
    void getDocumentViaFilter(String collectionName, Criteria criteria, ResultCallback callback);

}