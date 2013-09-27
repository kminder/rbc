/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.infra.reviewboard;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class ReviewBoard {

  private static final String REVIEW_BOARD_URL = "https://reviews.apache.org/";
  private static final String REVIEW_BOARD_USERNAME = "knoxq";
  private static final String REVIEW_BOARD_PASSWORD = "knoxqa4reviews";

  public static void main( String... args ) throws IOException {

    URL url = new URL( REVIEW_BOARD_URL );
    HttpHost host = new HttpHost( url.getHost(), url.getPort(), url.getProtocol() );

    Executor executor = Executor.newInstance()
        .auth( host, REVIEW_BOARD_USERNAME, REVIEW_BOARD_PASSWORD )
        .authPreemptive( host );

    Request request = Request.Get( REVIEW_BOARD_URL + "/api/review-requests/" );
    Response response = executor.execute( request );

    request = Request.Get( REVIEW_BOARD_URL + "/api/review-requests/" );
    response = executor.execute( request );

    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.readTree( response.returnResponse().getEntity().getContent() );

    JsonFactory factory = new JsonFactory();
    JsonGenerator generator = factory.createGenerator( new PrintWriter( System.out ) );
    generator.setPrettyPrinter( new DefaultPrettyPrinter(  ) );
    mapper.writeTree( generator, json );
  }
}
