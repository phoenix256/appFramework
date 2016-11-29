package tr.wolflame.framework.base.network.model.issueList;

import java.util.ArrayList;

import tr.wolflame.framework.base.objects.Issue;

/**
 * Created by SADIK on 14/09/15.
 */
public class ResponseIssueList {

    private String Status;
    private int PageIndex;
    private int PageSize;
    private int TotalCount;

    private ArrayList<Issue> IssuePackages;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public ArrayList<Issue> getIssueList() {
        return IssuePackages;
    }

    public void setIssueList(ArrayList<Issue> issuePackages) {
        IssuePackages = issuePackages;
    }
}
