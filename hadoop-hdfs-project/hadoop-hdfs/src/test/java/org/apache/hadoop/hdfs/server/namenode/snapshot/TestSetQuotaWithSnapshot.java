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
package org.apache.hadoop.hdfs.server.namenode.snapshot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.namenode.FSDirectory;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.hdfs.server.namenode.INodeDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestSetQuotaWithSnapshot {
  protected static final long seed = 0;
  protected static final short REPLICATION = 3;
  protected static final long BLOCKSIZE = 1024;
  
  protected Configuration conf;
  protected MiniDFSCluster cluster;
  protected FSNamesystem fsn;
  protected FSDirectory fsdir;
  protected DistributedFileSystem hdfs;
  
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    conf = new Configuration();
    conf.setLong(DFSConfigKeys.DFS_BLOCK_SIZE_KEY, BLOCKSIZE);
    cluster = new MiniDFSCluster.Builder(conf).numDataNodes(REPLICATION)
        .format(true).build();
    cluster.waitActive();

    fsn = cluster.getNamesystem();
    fsdir = fsn.getFSDirectory();
    hdfs = cluster.getFileSystem();
  }

  @After
  public void tearDown() throws Exception {
    if (cluster != null) {
      cluster.shutdown();
    }
  }
  
  @Test (timeout=60000)
  public void testSetQuota() throws Exception {
    final Path dir = new Path("/TestSnapshot");
    hdfs.mkdirs(dir);
    // allow snapshot on dir and create snapshot s1
    SnapshotTestHelper.createSnapshot(hdfs, dir, "s1");
    
    Path sub = new Path(dir, "sub");
    hdfs.mkdirs(sub);
    Path fileInSub = new Path(sub, "file");
    DFSTestUtil.createFile(hdfs, fileInSub, BLOCKSIZE, REPLICATION, seed);
    INodeDirectory subNode = INodeDirectory.valueOf(
        fsdir.getINode(sub.toString()), sub);
    // subNode should be a INodeDirectory, but not an INodeDirectoryWithSnapshot
    assertFalse(subNode instanceof INodeDirectoryWithSnapshot);
    
    hdfs.setQuota(sub, Long.MAX_VALUE - 1, Long.MAX_VALUE - 1);
    subNode = INodeDirectory.valueOf(fsdir.getINode(sub.toString()), sub);
    assertTrue(subNode.isQuotaSet());
    assertFalse(subNode instanceof INodeDirectoryWithSnapshot);
  }
}
