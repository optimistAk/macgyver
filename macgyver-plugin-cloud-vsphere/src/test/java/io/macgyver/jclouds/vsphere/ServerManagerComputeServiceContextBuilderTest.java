/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.jclouds.vsphere;


import io.macgyver.jclouds.vsphere.VSphereApiMetadata;

import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.internal.ContextImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class ServerManagerComputeServiceContextBuilderTest {


   @Test
   public void testCanBuildWithApiMetadata() {
      ComputeServiceContext context = ContextBuilder.newBuilder(
            new VSphereApiMetadata()).credentials("a","b").endpoint("https://somewhere").build(ComputeServiceContext.class);
      
      System.out.println(context.unwrap());
      
      context.close();
  
   }


   @Test
   public void testCanBuildById() {
	  
      ComputeServiceContext context = ContextBuilder.newBuilder("vsphere").credentials("a", "b").endpoint("https://somewhere").build(ComputeServiceContext.class);
      context.close();
   }

   @Ignore
   @Test
   public void testCanBuildWithOverridingProperties() {
      Properties overrides = new Properties();
      overrides.setProperty("servermanager.endpoint", "http://host");
      overrides.setProperty("servermanager.api-version", "1");

      ComputeServiceContext context = ContextBuilder.newBuilder("servermanager")
            .overrides(overrides).build(ComputeServiceContext.class);

      context.close();
   }


   @Test
   public void testUnwrapIsCorrectType() {
	   ComputeServiceContext context = ContextBuilder.newBuilder("vsphere").credentials("a", "b").endpoint("https://somewhere").build(ComputeServiceContext.class);
	      context.close();
      Assert.assertEquals(context.unwrap().getClass(), ContextImpl.class);

      context.close();
   }
}
