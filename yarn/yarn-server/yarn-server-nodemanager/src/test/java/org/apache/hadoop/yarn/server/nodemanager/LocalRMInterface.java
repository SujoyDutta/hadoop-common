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

package org.apache.hadoop.yarn.server.nodemanager;

import org.apache.avro.ipc.AvroRemoteException;
import org.apache.hadoop.yarn.HeartbeatResponse;
import org.apache.hadoop.yarn.NodeID;
import org.apache.hadoop.yarn.NodeStatus;
import org.apache.hadoop.yarn.RegistrationResponse;
import org.apache.hadoop.yarn.Resource;
import org.apache.hadoop.yarn.ResourceTracker;

public class LocalRMInterface implements ResourceTracker {

  @Override
  public RegistrationResponse registerNodeManager(CharSequence node,
      Resource resource) throws AvroRemoteException {
    RegistrationResponse registrationResponse = new RegistrationResponse();
    registrationResponse.nodeID = new NodeID();
    return registrationResponse;
  }

  @Override
  public HeartbeatResponse nodeHeartbeat(NodeStatus nodeStatus)
      throws AvroRemoteException {
    // TODO Auto-generated method stub
    return null;
  }

}