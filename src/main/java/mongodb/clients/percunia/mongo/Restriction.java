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

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * Created by linux on 4/11/16.
 */
public class Restriction {

    public static<T> Bson eq(String field, T value){
        return Filters.eq(field,value);
    }

    public static<T> Bson ne(String field, T value){
        return Filters.ne(field,value);
    }

    public static<T> Bson gt(String field, T value){
        return Filters.gt(field,value);
    }

    public static<T> Bson gte(String field, T value){
        return Filters.gte(field,value);
    }

    public static<T> Bson lt(String field, T value){
        return Filters.lt(field,value);
    }

    public static<T> Bson lte(String field, T value){
        return Filters.lte(field,value);
    }

    public static<T> Bson in(String field, List<T> value){
        return Filters.in(field,value);
    }

    public static<T> Bson nin(String field, List<T> value){
        return Filters.nin(field,value);
    }

    public static Bson and(Bson... restrictions){
        return Filters.and(restrictions);
    }

    public static Bson or(Bson... restrictions){
        return Filters.or(restrictions);
    }

}
