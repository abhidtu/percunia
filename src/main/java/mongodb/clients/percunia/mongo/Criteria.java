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
import com.mongodb.client.model.Projections;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * Created by linux on 4/11/16.
 */
public class Criteria {

    private List<Bson> restrictions;
    private long maxResults;
    private List<Bson> projections;

    public Criteria add(Bson restriction){
        restrictions.add(restriction);
        return this;
    }

    public Criteria setRestrictions(List<Bson> restrictions) {
        this.restrictions = restrictions;
        return this;
    }

    public Bson getRestrictions(){
        return Filters.and(restrictions);
    }

    public void addProjection(Bson projection){
        this.projections.add(projection);
    }

    public Bson getProjections() {
        return Projections.fields(projections);
    }

    public void setProjections(List<Bson> projections) {
        this.projections = projections;
    }

    public long getMaxResults() {
        return maxResults;
    }

    public Criteria setMaxResults(long maxResults) {
        this.maxResults = maxResults;
        return this;
    }
}
