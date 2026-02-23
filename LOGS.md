# Logs

## When network connectivity is broken

There are **NO errors**; however the following INFO log can be seen every 20 seconds or so.

```log
2026-02-23T09:17:17.217+05:30  INFO 2744 --- [gcp-pubsub-demo] [bscriber-SE-1-3] c.g.c.p.v.StreamingSubscriberConnection  : No response from server for 20 seconds since last ping. Closing stream.
```

## when subscription does not exist, AND there is no "failed" handling

```log
2026-02-23T10:01:34.601+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] c.e.g.p.demo.GcpPubsubDemoApplication    : Starting GcpPubsubDemoApplication using Java 21.0.9 with PID 11116 (C:\_home\repos\gcp-pubsub-demo\target\classes started by gulen in C:\_home\repos\gcp-pubsub-demo)
2026-02-23T10:01:34.605+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] c.e.g.p.demo.GcpPubsubDemoApplication    : No active profile set, falling back to 1 default profile: "default"
2026-02-23T10:01:34.680+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2026-02-23T10:01:34.681+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2026-02-23T10:01:37.012+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2026-02-23T10:01:37.034+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-02-23T10:01:37.034+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.50]
2026-02-23T10:01:37.125+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2026-02-23T10:01:37.126+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2443 ms
2026-02-23T10:01:37.572+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] c.g.c.s.a.c.GcpContextAutoConfiguration  : The default project ID is project-602c1df5-e612-4947-a79
2026-02-23T10:01:37.618+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] c.g.c.s.core.DefaultCredentialsProvider  : Default credentials provider for service account myapp-catalog-subscriber@project-602c1df5-e612-4947-a79.iam.gserviceaccount.com
2026-02-23T10:01:37.618+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] c.g.c.s.core.DefaultCredentialsProvider  : Scopes in use by default credentials: [https://www.googleapis.com/auth/pubsub, https://www.googleapis.com/auth/spanner.admin, https://www.googleapis.com/auth/spanner.data, https://www.googleapis.com/auth/datastore, https://www.googleapis.com/auth/sqlservice.admin, https://www.googleapis.com/auth/devstorage.read_only, https://www.googleapis.com/auth/devstorage.read_write, https://www.googleapis.com/auth/cloudruntimeconfig, https://www.googleapis.com/auth/trace.append, https://www.googleapis.com/auth/cloud-platform, https://www.googleapis.com/auth/cloud-vision, https://www.googleapis.com/auth/bigquery, https://www.googleapis.com/auth/monitoring.write]
2026-02-23T10:01:38.652+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] c.e.gcp.pubsub.demo.CatalogSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:01:39.530+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2026-02-23T10:01:39.542+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2026-02-23T10:01:39.609+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2026-02-23T10:01:39.626+05:30  INFO 11116 --- [gcp-pubsub-demo] [  restartedMain] c.e.g.p.demo.GcpPubsubDemoApplication    : Started GcpPubsubDemoApplication in 5.565 seconds (process running for 6.589)
2026-02-23T10:01:40.283+05:30  INFO 11116 --- [gcp-pubsub-demo] [on(2)-127.0.0.1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2026-02-23T10:01:40.284+05:30  INFO 11116 --- [gcp-pubsub-demo] [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2026-02-23T10:01:40.285+05:30  INFO 11116 --- [gcp-pubsub-demo] [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-02-23T10:01:43.116+05:30 ERROR 11116 --- [gcp-pubsub-demo] [bscriber-SE-1-0] c.g.c.p.v.StreamingSubscriberConnection  : terminated streaming with exception

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:01:43.128+05:30  WARN 11116 --- [gcp-pubsub-demo] [bscriber-SE-1-0] c.g.c.p.v.StreamingSubscriberConnection  : Setting response: SUCCESSFUL on outstanding messages
2026-02-23T10:03:56.068+05:30  INFO 11116 --- [gcp-pubsub-demo] [ionShutdownHook] o.s.b.w.e.tomcat.GracefulShutdown        : Commencing graceful shutdown. Waiting for active requests to complete
2026-02-23T10:03:56.078+05:30  INFO 11116 --- [gcp-pubsub-demo] [tomcat-shutdown] o.s.b.w.e.tomcat.GracefulShutdown        : Graceful shutdown complete
2026-02-23T10:03:56.096+05:30  INFO 11116 --- [gcp-pubsub-demo] [ionShutdownHook] c.e.gcp.pubsub.demo.CatalogSubscriber    : Stopping subscriber for catalog-subscription-test
2026-02-23T10:03:56.098+05:30  WARN 11116 --- [gcp-pubsub-demo] [ionShutdownHook] .s.c.a.CommonAnnotationBeanPostProcessor : Destroy method on bean with name 'catalogSubscriber' threw an exception: java.lang.IllegalStateException: Expected the service InnerService [FAILED] to be TERMINATED, but the service has FAILED

```

## With "failed" handling

```log
2026-02-23T10:10:10.020+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] c.e.g.p.demo.GcpPubsubDemoApplication    : Starting GcpPubsubDemoApplication using Java 21.0.9 with PID 22184 (C:\_home\repos\gcp-pubsub-demo\target\classes started by gulen in C:\_home\repos\gcp-pubsub-demo)
2026-02-23T10:10:10.023+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] c.e.g.p.demo.GcpPubsubDemoApplication    : No active profile set, falling back to 1 default profile: "default"
2026-02-23T10:10:10.098+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2026-02-23T10:10:10.098+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2026-02-23T10:10:11.910+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2026-02-23T10:10:11.932+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-02-23T10:10:11.933+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.50]
2026-02-23T10:10:12.027+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2026-02-23T10:10:12.028+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1928 ms
2026-02-23T10:10:12.418+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] c.g.c.s.a.c.GcpContextAutoConfiguration  : The default project ID is project-602c1df5-e612-4947-a79
2026-02-23T10:10:12.471+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] c.g.c.s.core.DefaultCredentialsProvider  : Default credentials provider for service account myapp-catalog-subscriber@project-602c1df5-e612-4947-a79.iam.gserviceaccount.com
2026-02-23T10:10:12.472+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] c.g.c.s.core.DefaultCredentialsProvider  : Scopes in use by default credentials: [https://www.googleapis.com/auth/pubsub, https://www.googleapis.com/auth/spanner.admin, https://www.googleapis.com/auth/spanner.data, https://www.googleapis.com/auth/datastore, https://www.googleapis.com/auth/sqlservice.admin, https://www.googleapis.com/auth/devstorage.read_only, https://www.googleapis.com/auth/devstorage.read_write, https://www.googleapis.com/auth/cloudruntimeconfig, https://www.googleapis.com/auth/trace.append, https://www.googleapis.com/auth/cloud-platform, https://www.googleapis.com/auth/cloud-vision, https://www.googleapis.com/auth/bigquery, https://www.googleapis.com/auth/monitoring.write]
2026-02-23T10:10:13.244+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:10:14.139+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2026-02-23T10:10:14.151+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2026-02-23T10:10:14.220+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2026-02-23T10:10:14.252+05:30  INFO 22184 --- [gcp-pubsub-demo] [  restartedMain] c.e.g.p.demo.GcpPubsubDemoApplication    : Started GcpPubsubDemoApplication in 4.708 seconds (process running for 5.61)
2026-02-23T10:10:15.724+05:30  INFO 22184 --- [gcp-pubsub-demo] [on(2)-127.0.0.1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2026-02-23T10:10:15.725+05:30  INFO 22184 --- [gcp-pubsub-demo] [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2026-02-23T10:10:15.726+05:30  INFO 22184 --- [gcp-pubsub-demo] [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2026-02-23T10:10:42.023+05:30  INFO 22184 --- [gcp-pubsub-demo] [sub-subscriber1] c.e.g.p.demo.AbstractPubSubSubscriber    : Handling message for catalog-subscription-test, payload: {}
2026-02-23T10:10:42.024+05:30  INFO 22184 --- [gcp-pubsub-demo] [sub-subscriber1] c.e.gcp.pubsub.demo.CatalogSubscriber    : Processing message: [{}]
2026-02-23T10:11:20.589+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-1-0] c.g.c.p.v.StreamingSubscriberConnection  : terminated streaming with exception

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:11:20.597+05:30  WARN 22184 --- [gcp-pubsub-demo] [bscriber-SE-1-0] c.g.c.p.v.StreamingSubscriberConnection  : Setting response: SUCCESSFUL on outstanding messages
2026-02-23T10:11:20.603+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-1-1] c.e.g.p.demo.AbstractPubSubSubscriber    : Subscriber for catalog-subscription-test failed (was in state RUNNING), scheduling reconnect in 30s

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:11:20.610+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscriber-SE-1-1] c.e.g.p.demo.AbstractPubSubSubscriber    : Reconnect for catalog-subscription-test scheduled in 30s
2026-02-23T10:11:50.629+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:11:53.540+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-2-2] c.g.c.p.v.StreamingSubscriberConnection  : terminated streaming with exception

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:11:53.551+05:30  WARN 22184 --- [gcp-pubsub-demo] [bscriber-SE-2-2] c.g.c.p.v.StreamingSubscriberConnection  : Setting response: SUCCESSFUL on outstanding messages
2026-02-23T10:11:53.552+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-2-2] c.e.g.p.demo.AbstractPubSubSubscriber    : Subscriber for catalog-subscription-test failed (was in state RUNNING), scheduling reconnect in 30s

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:11:53.558+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscriber-SE-2-2] c.e.g.p.demo.AbstractPubSubSubscriber    : Reconnect for catalog-subscription-test scheduled in 30s
2026-02-23T10:12:13.266+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:12:15.670+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-3-2] c.g.c.p.v.StreamingSubscriberConnection  : terminated streaming with exception

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:15.677+05:30  WARN 22184 --- [gcp-pubsub-demo] [bscriber-SE-3-2] c.g.c.p.v.StreamingSubscriberConnection  : Setting response: SUCCESSFUL on outstanding messages
2026-02-23T10:12:15.679+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-3-4] c.e.g.p.demo.AbstractPubSubSubscriber    : Subscriber for catalog-subscription-test failed (was in state RUNNING), scheduling reconnect in 30s

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=projects/project-602c1df5-e612-4947-a79/subscriptions/catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:15.683+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscriber-SE-3-4] c.e.g.p.demo.AbstractPubSubSubscriber    : Reconnect for catalog-subscription-test scheduled in 30s
2026-02-23T10:12:23.566+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:12:25.735+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-4-3] c.g.c.p.v.StreamingSubscriberConnection  : terminated streaming with exception

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:25.746+05:30  WARN 22184 --- [gcp-pubsub-demo] [bscriber-SE-4-3] c.g.c.p.v.StreamingSubscriberConnection  : Setting response: SUCCESSFUL on outstanding messages
2026-02-23T10:12:25.747+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-4-3] c.e.g.p.demo.AbstractPubSubSubscriber    : Subscriber for catalog-subscription-test failed (was in state RUNNING), scheduling reconnect in 30s

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:25.753+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscriber-SE-4-3] c.e.g.p.demo.AbstractPubSubSubscriber    : Reconnect for catalog-subscription-test scheduled in 30s
2026-02-23T10:12:45.704+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:12:48.084+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-5-5] c.g.c.p.v.StreamingSubscriberConnection  : terminated streaming with exception

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:48.107+05:30  WARN 22184 --- [gcp-pubsub-demo] [bscriber-SE-5-5] c.g.c.p.v.StreamingSubscriberConnection  : Setting response: SUCCESSFUL on outstanding messages
2026-02-23T10:12:48.108+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-5-1] c.e.g.p.demo.AbstractPubSubSubscriber    : Subscriber for catalog-subscription-test failed (was in state RUNNING), scheduling reconnect in 30s

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:48.118+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscriber-SE-5-1] c.e.g.p.demo.AbstractPubSubSubscriber    : Reconnect for catalog-subscription-test scheduled in 30s
2026-02-23T10:12:55.769+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:12:58.081+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-6-2] c.g.c.p.v.StreamingSubscriberConnection  : terminated streaming with exception

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:58.088+05:30  WARN 22184 --- [gcp-pubsub-demo] [bscriber-SE-6-2] c.g.c.p.v.StreamingSubscriberConnection  : Setting response: SUCCESSFUL on outstanding messages
2026-02-23T10:12:58.092+05:30 ERROR 22184 --- [gcp-pubsub-demo] [bscriber-SE-6-2] c.e.g.p.demo.AbstractPubSubSubscriber    : Subscriber for catalog-subscription-test failed (was in state RUNNING), scheduling reconnect in 30s

com.google.api.gax.rpc.NotFoundException: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$1.onFailure(StreamingSubscriberConnection.java:423) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.core.ApiFutures$1.onFailure(ApiFutures.java:84) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1125) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.DirectExecutor.execute(DirectExecutor.java:30) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.executeListener(AbstractFuture.java:1004) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.complete(AbstractFuture.java:767) ~[guava-33.5.0-jre.jar:na]
        at com.google.common.util.concurrent.AbstractFuture.setException(AbstractFuture.java:516) ~[guava-33.5.0-jre.jar:na]
        at com.google.api.core.AbstractApiFuture$InternalSettableFuture.setException(AbstractApiFuture.java:92) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.AbstractApiFuture.setException(AbstractApiFuture.java:74) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.api.core.SettableApiFuture.setException(SettableApiFuture.java:51) ~[api-common-2.57.0.jar:2.57.0]
        at com.google.cloud.pubsub.v1.StreamingSubscriberConnection$StreamingPullResponseObserver.onError(StreamingSubscriberConnection.java:329) ~[google-cloud-pubsub-1.147.0.jar:1.147.0]
        at com.google.api.gax.tracing.TracedResponseObserver.onError(TracedResponseObserver.java:104) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:84) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.StateCheckingResponseObserver.onError(StateCheckingResponseObserver.java:84) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcDirectStreamController$ResponseObserverAdapter.onClose(GrpcDirectStreamController.java:148) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.ChannelPool$ReleasingClientCall$1.onClose(ChannelPool.java:602) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.PartialForwardingClientCallListener.onClose(PartialForwardingClientCallListener.java:39) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener.onClose(ForwardingClientCallListener.java:23) ~[grpc-api-1.76.2.jar:1.76.2]
        at io.grpc.ForwardingClientCallListener$SimpleForwardingClientCallListener.onClose(ForwardingClientCallListener.java:40) ~[grpc-api-1.76.2.jar:1.76.2]
        at com.google.api.gax.grpc.GrpcLoggingInterceptor$1$1.onClose(GrpcLoggingInterceptor.java:98) ~[gax-grpc-2.74.0.jar:2.74.0]
        at io.grpc.internal.DelayedClientCall$DelayedListener$3.run(DelayedClientCall.java:487) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.delayOrExecute(DelayedClientCall.java:451) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.DelayedClientCall$DelayedListener.onClose(DelayedClientCall.java:484) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:565) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl.access$100(ClientCallImpl.java:72) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal(ClientCallImpl.java:733) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext(ClientCallImpl.java:714) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) ~[grpc-core-1.76.2.jar:1.76.2]
        at io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) ~[grpc-core-1.76.2.jar:1.76.2]
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[na:na]
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[na:na]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]
Caused by: com.google.api.gax.rpc.NotFoundException: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:90) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.rpc.ApiExceptionFactory.createException(ApiExceptionFactory.java:41) ~[gax-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:86) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.GrpcApiExceptionFactory.create(GrpcApiExceptionFactory.java:66) ~[gax-grpc-2.74.0.jar:2.74.0]
        at com.google.api.gax.grpc.ExceptionResponseObserver.onErrorImpl(ExceptionResponseObserver.java:82) ~[gax-grpc-2.74.0.jar:2.74.0]
        ... 25 common frames omitted
Caused by: io.grpc.StatusRuntimeException: NOT_FOUND: Resource not found (resource=catalog-subscription-test).
        at io.grpc.Status.asRuntimeException(Status.java:532) ~[grpc-api-1.76.2.jar:1.76.2]
        ... 24 common frames omitted

2026-02-23T10:12:58.097+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscriber-SE-6-2] c.e.g.p.demo.AbstractPubSubSubscriber    : Reconnect for catalog-subscription-test scheduled in 30s
2026-02-23T10:13:13.253+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:13:18.123+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:13:23.216+05:30  INFO 22184 --- [gcp-pubsub-demo] [sub-subscriber1] c.e.g.p.demo.AbstractPubSubSubscriber    : Handling message for catalog-subscription-test, payload: {}
2026-02-23T10:13:23.216+05:30  INFO 22184 --- [gcp-pubsub-demo] [sub-subscriber1] c.e.gcp.pubsub.demo.CatalogSubscriber    : Processing message: [{}]
2026-02-23T10:13:28.111+05:30  INFO 22184 --- [gcp-pubsub-demo] [bscription-test] c.e.g.p.demo.AbstractPubSubSubscriber    : Started subscriber for catalog-subscription-test
2026-02-23T10:13:52.242+05:30  INFO 22184 --- [gcp-pubsub-demo] [sub-subscriber2] c.e.g.p.demo.AbstractPubSubSubscriber    : Handling message for catalog-subscription-test, payload: {}
2026-02-23T10:13:52.243+05:30  INFO 22184 --- [gcp-pubsub-demo] [sub-subscriber2] c.e.gcp.pubsub.demo.CatalogSubscriber    : Processing message: [{}]
2026-02-23T10:15:43.246+05:30  INFO 22184 --- [gcp-pubsub-demo] [ionShutdownHook] o.s.b.w.e.tomcat.GracefulShutdown        : Commencing graceful shutdown. Waiting for active requests to complete
2026-02-23T10:15:43.254+05:30  INFO 22184 --- [gcp-pubsub-demo] [tomcat-shutdown] o.s.b.w.e.tomcat.GracefulShutdown        : Graceful shutdown complete
2026-02-23T10:15:43.270+05:30  INFO 22184 --- [gcp-pubsub-demo] [ionShutdownHook] c.e.g.p.demo.AbstractPubSubSubscriber    : Stopping subscriber for catalog-subscription-test
2026-02-23T10:15:43.272+05:30  INFO 22184 --- [gcp-pubsub-demo] [ionShutdownHook] c.e.g.p.demo.AbstractPubSubSubscriber    : Stopped subscriber for catalog-subscription-test
```
