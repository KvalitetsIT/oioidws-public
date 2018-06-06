package client.session;

import dk.sds.samlh.model.resourceid.ResourceId;

public class SessionContext {
	private ResourceId resourceId;

	public ResourceId getResourceId() {
		return resourceId;
	}

	public void setResourceId(ResourceId resourceId) {
		this.resourceId = resourceId;
	}
}
