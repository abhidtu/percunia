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

import com.mongodb.client.model.Projections;
import org.bson.conversions.Bson;

/**
 * Created by linux on 4/11/16.
 */
public class Projection {

    public static Bson include(String... fields){
        return Projections.include(fields);
    }

    public static Bson exclude(String... fields){
        return Projections.exclude(fields);
    }

    public static Bson elematch(String field){
        return Projections.elemMatch(field);
    }

    public static Bson elemMatch(String field, Bson restriction) {
        return Projections.elemMatch(field,restriction);
    }

    public static Bson slice(String field, int limit){
        return Projections.slice(field,limit);
    }

    public static Bson slice(String field, int start, int end){
        return Projections.slice(field,start,end);
    }

    public static Bson metaTextScore(String field){
        return Projections.metaTextScore(field);
    }
}
