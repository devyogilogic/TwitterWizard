
package com.developer.twitterwizard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopPositive {

    @SerializedName("top_positive")
    @Expose
    private String topPositive;

    public String getTopPositive() {
        return topPositive;
    }

    public void setTopPositive(String topPositive) {
        this.topPositive = topPositive;
    }

}
