/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.metrics.serialization.thrift;

import com.ning.metrics.serialization.thrift.item.DataItem;
import com.ning.metrics.serialization.thrift.item.DataItemDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TType;
import org.apache.thrift.transport.TIOStreamTransport;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class ThriftFieldListDeserializer
{
    private final DataItemDeserializer dataItemDeserializer = new DataItemDeserializer();

    public List<ThriftField> readPayload(final byte[] payload) throws TException
    {
        final List<ThriftField> thriftFieldList = new ArrayList<ThriftField>();

        final ByteArrayInputStream payloadInputStream = new ByteArrayInputStream(payload);
        final TProtocol payloadProtocol = new TBinaryProtocol(new TIOStreamTransport(payloadInputStream));
        payloadProtocol.readStructBegin();

        TField payloadField = payloadProtocol.readFieldBegin();

        // TODO Should we enforce starting at 0/1 instead?
        short expectedFieldNb = payloadField.id;
        while (payloadField.type != TType.STOP) {
            // Handle missing fields
            if (payloadField.id != expectedFieldNb) {
                thriftFieldList.add(new ThriftFieldImpl(null, expectedFieldNb));
            }
            else {
                final DataItem dataItem = dataItemDeserializer.fromThrift(payloadProtocol, payloadField);
                thriftFieldList.add(new ThriftFieldImpl(dataItem, payloadField));
                payloadProtocol.readFieldEnd();
                payloadField = payloadProtocol.readFieldBegin();
            }
            expectedFieldNb++;
        }

        payloadProtocol.readStructEnd();
        payloadProtocol.getTransport().close();

        return thriftFieldList;
    }
}
