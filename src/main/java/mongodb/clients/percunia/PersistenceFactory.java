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

import mongodb.clients.percunia.mongo.AsyncClient;

/**
 * Created by linux on 30/5/16.
 */
public class PersistenceFactory {

    public static Ipersistence getClient(){

        if(Config.DRIVER.equalsIgnoreCase("mongo")){
            return AsyncClient.getInstance();
        } else if(Config.DRIVER.equalsIgnoreCase("redis")){
            return null;
        }

        return null;
    }

}
