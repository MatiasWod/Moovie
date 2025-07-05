package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;

public class ReportCreateDto {
    @NotNull
    private int type;

    @NotNull
    private int resourceId;

    @NotNull
    private int resourceType;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }
}
