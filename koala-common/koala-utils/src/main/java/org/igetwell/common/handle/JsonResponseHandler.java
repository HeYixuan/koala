package org.igetwell.common.handle;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.igetwell.common.uitls.GsonUtils;

import java.io.IOException;

@Slf4j
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
                HttpEntity entity = response.getEntity();
                String str = EntityUtils.toString(entity,"UTF-8");
                log.info("URI[{}] RESPONSE DATA:{}", str);
                return GsonUtils.fromJson(str, clazz);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    }
}
