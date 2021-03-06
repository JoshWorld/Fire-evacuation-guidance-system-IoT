/*
 * //******************************************************************
 * //
 * // Copyright 2016 Samsung Electronics All Rights Reserved.
 * //
 * //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * //      http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * //
 * //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
 */
package org.iotivity.cloud.ciserver.resources.proxy.account;

import java.util.Arrays;
import java.util.HashMap;

import org.iotivity.cloud.base.connector.ConnectorPool;
import org.iotivity.cloud.base.device.Device;
import org.iotivity.cloud.base.device.IRequestChannel;
import org.iotivity.cloud.base.exception.ServerException;
import org.iotivity.cloud.base.exception.ServerException.BadRequestException;
import org.iotivity.cloud.base.protocols.IRequest;
import org.iotivity.cloud.base.protocols.MessageBuilder;
import org.iotivity.cloud.base.resource.Resource;
import org.iotivity.cloud.ciserver.Constants;
import org.iotivity.cloud.util.Cbor;

/**
 *
 * This class provides a set of APIs to send requests about invite to account
 *
 */

public class AclInvite extends Resource {

    public AclInvite() {
        super(Arrays.asList(Constants.PREFIX_OIC, Constants.ACL_URI,
                Constants.INVITE_URI));
    }

    @Override
    public void onDefaultRequestReceived(Device srcDevice, IRequest request)
            throws ServerException {

        switch (request.getMethod()) {

            case POST:

                Cbor<HashMap<String, Object>> cbor = new Cbor<>();
                HashMap<String, Object> payloadData = cbor.parsePayloadFromCbor(
                        request.getPayload(), HashMap.class);

                checkPayloadException(Constants.REQ_INVITE, payloadData);
                break;
            case GET:
                break;
            case DELETE:
                break;
            default:
                throw new BadRequestException(
                        request.getMethod() + " request type is not support");
        }
        StringBuffer additionalQuery = new StringBuffer();
        additionalQuery.append(Constants.USER_ID + "=" + srcDevice.getUserId());

        String uriQuery = additionalQuery.toString()
                + (request.getUriQuery() != null ? (";" + request.getUriQuery())
                        : "");
        request = MessageBuilder.modifyRequest(request, null, uriQuery, null,
                null);

        ConnectorPool.getConnection("account").sendRequest(request, srcDevice);
    }

}