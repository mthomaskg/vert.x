/*
 * Copyright (c) 2011-2014 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.test.core;

import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.RequestOptions;
import org.junit.Test;

/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class HttpCompressionTest extends HttpTestBase {

  public void setUp() throws Exception {
    super.setUp();
    client = vertx.createHttpClient(HttpClientOptions.options().setTryUseCompression(true));
    server = vertx.createHttpServer(HttpServerOptions.options().setPort(DEFAULT_HTTP_PORT).setCompressionSupported(true));
  }

  @Test
  public void testDefaultRequestHeaders() {
    server.requestHandler(req -> {
      assertEquals(2, req.headers().size());
      assertEquals("localhost:" + DEFAULT_HTTP_PORT, req.headers().get("host"));
      assertNotNull(req.headers().get("Accept-Encoding"));
      req.response().end();
    });

    server.listen(onSuccess(server -> {
      client.getNow(RequestOptions.options().setRequestURI("some-uri").setPort(DEFAULT_HTTP_PORT), resp -> testComplete());
    }));

    await();
  }
}
