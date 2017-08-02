/*
 * Copyright 2016 Hortonworks.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hortonworks.registries.schemaregistry.serde;

import com.hortonworks.registries.schemaregistry.client.ISchemaRegistryClient;
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 */
public abstract class AbstractSerDes {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSerDes.class);

    protected ISchemaRegistryClient schemaRegistryClient;
    protected boolean initialized = false;
    protected boolean closed = false;

    public AbstractSerDes() {
        this(null);
    }

    public AbstractSerDes(ISchemaRegistryClient schemaRegistryClient) {
        this.schemaRegistryClient = schemaRegistryClient;
    }

    public final void init(Map<String, ?> config) {
        if (closed) {
            throw new IllegalStateException("Closed instance can not be initialized again");
        }
        if (initialized) {
            LOG.info("This instance [{}] is already inited", this);
            return;
        }

        LOG.debug("Initialized with config: [{}]", config);
        if (schemaRegistryClient == null) {
            schemaRegistryClient = new SchemaRegistryClient(config);
        }

        doInit(config);

        initialized = true;
    }

    protected void doInit(Map<String, ?> config) {
    }

    public void close() throws Exception {
        if (closed) {
            LOG.info("This instance [{}] is already closed", this);
            return;
        }
        try {
            if (schemaRegistryClient != null) {
                schemaRegistryClient.close();
            }
        } finally {
            closed = true;
        }
    }

}
