package org.igetwell.common.handle;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.igetwell.common.uitls.GsonUtils;

import java.io.IOException;

public class JsonResponseHandler {

    public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz){
        return new JsonResponseHandlerImpl<T>(clazz);
    }

    public static class JsonResponseHandlerImpl<T>implements ResponseHandler<T> {

        private Class<T> clazz;

        public JsonResponseHandlerImpl(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                String str = EntityUtils.toString(response.getEntity(),"UTF-8");
                //log.info("URI[{}] RESPONSE DATA:{}", str);
                return GsonUtils.fromJson(str, clazz);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    }
}
