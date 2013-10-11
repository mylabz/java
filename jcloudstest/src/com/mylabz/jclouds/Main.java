package com.mylabz.jclouds;
import java.util.Set;

import org.jclouds.aws.ec2.*;
import org.jclouds.aws.ec2.compute.*;
import org.jclouds.compute.*;
import org.jclouds.compute.domain.*;
import org.jclouds.compute.domain.Image;
import org.jclouds.ec2.domain.*;
import org.jclouds.domain.*;
import com.google.common.collect.*;
import org.jclouds.sshj.config.SshjSshClientModule;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// get a context with ec2 that offers the portable ComputeService API
		ComputeServiceContext context =
		                new ComputeServiceContextFactory().createContext("aws-ec2",
		                                                                "123access",
		                                                                "123secret",
		                                                                ImmutableSet.<Module> of(new Log4JLoggingModule(),
		                                                                new SshjSshClientModule()));

		// here's an example of the portable api
		Set<? extends Location> locations =
		        context.getComputeService().listAssignableLocations();

		Set<? extends Image> images = context.getComputeService().listImages();

		// pick the highest version of the RightScale CentOS template
		Template template = context.getComputeService().templateBuilder().osFamily(OsFamily.CENTOS).build();

		// specify your own groups which already have the correct rules applied
		template.getOptions().as(AWSEC2TemplateOptions.class).securityGroups("test");

		// specify your own keypair for use in creating nodes
		template.getOptions().as(AWSEC2TemplateOptions.class).keyPair(keyPair);

		// run a couple nodes accessible via group
		Set<? extends NodeMetadata> nodes = context.getComputeService().createNodesInGroup("webserver", 2, template);

		// when you need access to very ec2-specific features, use the provider-specific context
		AWSEC2Client ec2Client = AWSEC2Client.class.cast(context.getProviderSpecificContext().getApi());

		// ex. to get an ip and associate it with a node
		NodeMetadata node = Iterables.get(nodes, 0);
		String ip = ec2Client.getElasticIPAddressServices().allocateAddressInRegion(node.getLocation().getId());
		ec2Client.getElasticIPAddressServices().associateAddressInRegion(node.getLocation().getId(),ip, node.getProviderId());

		context.close();
		
	}

}
