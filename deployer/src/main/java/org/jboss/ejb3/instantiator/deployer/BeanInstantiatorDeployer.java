/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
  *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ejb3.instantiator.deployer;

import org.jboss.beans.metadata.api.annotations.Start;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;
import org.jboss.logging.Logger;
import org.jboss.metadata.ejb.jboss.JBossEnterpriseBeanMetaData;
import org.jboss.metadata.ejb.jboss.JBossMetaData;

/**
 * VDF Deployer to attach a {@link BeanInstantiator} implementation
 * to the current EJB3 {@link DeploymentUnit}
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */

public class BeanInstantiatorDeployer extends AbstractBeanInstantiatorDeployer
{

   // ------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   // ------------------------------------------------------------------------------||
   // Instance Members -------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * {@link BeanInstantiator} implementation to attach to the {@link DeploymentUnit}
    * if it's an EJB3 deployment
    */
   private final BeanInstantiator beanInstantiator;

   // ------------------------------------------------------------------------------||
   // Constructor ------------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   public BeanInstantiatorDeployer(final BeanInstantiator beanInstantiator)
   {
      super();
      this.beanInstantiator = beanInstantiator;
   }

   public void setBeanInstantiatorLocator(BeanInstantiatorLocator beanInstantiatorLocator)
   {
      this.beanInstantiatorLocator = beanInstantiatorLocator;
   }

   // ------------------------------------------------------------------------------||
   // Required Implementations -----------------------------------------------------||
   // ------------------------------------------------------------------------------||

   @Override
   protected BeanInstantiator createBeanInstantiator(JBossEnterpriseBeanMetaData jBossEnterpriseBeanMetaData)
   {
      return beanInstantiator;
   }

   @Start
   public void validateOnStart()
   {
      // Ensure the instantiator was injected
     if (createBeanInstantiator(null) == null)
     {
        throw new IllegalStateException("Bean instantiator implemenentation was not injected");
     }
   }

   // ------------------------------------------------------------------------------||
   // Helper Methods ---------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /*
    * These may be overridden for testing purposes
    */

}
