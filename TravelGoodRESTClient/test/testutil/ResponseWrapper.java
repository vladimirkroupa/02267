package testutil;

import com.sun.jersey.api.client.ClientResponse;

/**
 * Response entity and response HTTP status code. Wraps ClientReponse and its entity.
 *
 * @param <T> wrapped entity type (e.g. HotelList)
 */
public class ResponseWrapper<T> {
    private final Class<T> entityClass;
    private final ClientResponse resp;

    public ResponseWrapper(Class<T> entityClass, ClientResponse resp) {
        this.entityClass = entityClass;
        this.resp = resp;
    }

    public T entity() {
        return resp.getEntity(entityClass);
    }

    public int status() {
        return resp.getStatus();
    }
    
}
