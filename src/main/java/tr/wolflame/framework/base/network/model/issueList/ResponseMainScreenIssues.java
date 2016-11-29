package tr.wolflame.framework.base.network.model.issueList;

import java.util.List;

import tr.wolflame.framework.base.objects.Issue;

/**
 * Created by SADIK on 14/09/15.
 */
public class ResponseMainScreenIssues {

    private String Status;

    private List<Issue> Issues;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<Issue> getIssuePackages() {
        return Issues;
    }

    public void setIssuePackages(List<Issue> issues) {
        Issues = issues;
    }
}
