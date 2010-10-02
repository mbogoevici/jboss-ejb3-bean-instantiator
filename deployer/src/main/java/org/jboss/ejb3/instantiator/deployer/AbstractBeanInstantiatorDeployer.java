package org.jboss.ejb3.instantiator.deployer;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;
import org.jboss.logging.Logger;
import org.jboss.metadata.ejb.jboss.JBossEnterpriseBeanMetaData;
import org.jboss.metadata.ejb.jboss.JBossMetaData;

/**
 * @author Marius Bogoevici
 */
public abstract class AbstractBeanInstantiatorDeployer extends AbstractDeployer
{
   /**
    * Logger
    */
   private static final Logger log = Logger.getLogger(BeanInstantiatorDeployer.class);
   protected BeanInstantiatorLocator beanInstantiatorLocator;

   protected AbstractBeanInstantiatorDeployer()
   {
      // set the deployer to pick up EJB deployments during POST_CLASSLOADER stage
      this.setStage(DeploymentStages.POST_CLASSLOADER);
      this.setInput(JBossMetaData.class);
   }

   /**
    * {@inheritDoc}
    *
    * @see org.jboss.deployers.spi.deployer.Deployer#deploy(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   public void deploy(final DeploymentUnit unit) throws DeploymentException
   {
      // If not an EJB3 deployment, take no action
      if (!this.isEjb3Deployment(unit))
      {
         return;
      }
      
      JBossMetaData jBossMetaData = unit.getAttachment(JBossMetaData.class);
      for (JBossEnterpriseBeanMetaData jBossEnterpriseBeanMetaData : jBossMetaData.getEnterpriseBeans())
      {
         //perhaps we should let MC take care of the instantiation, but how would it be passed to the EJBContainer then ?
         // or in other words, how
         BeanInstantiator instantiator = createBeanInstantiator(jBossEnterpriseBeanMetaData);
         unit.addAttachment(beanInstantiatorLocator.getInstantiatorBeanName(unit, jBossEnterpriseBeanMetaData),
               createBeanInstantiator(jBossEnterpriseBeanMetaData));
         if (log.isTraceEnabled())
         {

            log.trace("Using bean instantiator " + instantiator + " for component "
                  + jBossEnterpriseBeanMetaData.getEjbName() + " of unit " + unit);
         }
      }

   }

   protected abstract BeanInstantiator createBeanInstantiator(JBossEnterpriseBeanMetaData jBossEnterpriseBeanMetaData);

   /**
    * Returns whether this is an EJB3 Deployment, determining if we should take action
    *
    * @param unit
    * @return
    */
   boolean isEjb3Deployment(final DeploymentUnit unit)
   {
      // Obtain the Merged Metadata
      final JBossMetaData md = unit.getAttachment(JBossMetaData.class);

      // If metadata's not present as an attachment, return
      if (md == null)
      {
         return false;
      }

      // If this is not an EJB3 Deployment, return
      if (!md.isEJB3x())
      {
         return false;
      }

      // Meets conditions
      return true;
   }
}
