/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author ptd
 */
public class Feedback implements Serializable{
    private int feedbackId;
    private int accountId;
    private String accountName;
    private String feedbackDate;
    private String content;
    private String rating;
    private String replyComment;
    private String authorReply;
    private boolean active;

    public Feedback() {
    }

    public Feedback(int feedbackId, int accountId, String feedbackDate, String content, String rating) {
        this.feedbackId = feedbackId;
        this.accountId = accountId;
        this.feedbackDate = feedbackDate;
        this.content = content;
        this.rating = rating;
    }

    public Feedback(int feedbackId, int accountId, String accountName, String feedbackDate, String content, String rating) {
        this.feedbackId = feedbackId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.feedbackDate = feedbackDate;
        this.content = content;
        this.rating = rating;
    }

    public Feedback(int feedbackId, int accountId, String accountName, String feedbackDate, String content, String rating, String replyComment, String authorReply) {
        this.feedbackId = feedbackId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.feedbackDate = feedbackDate;
        this.content = content;
        this.rating = rating;
        this.replyComment = replyComment;
        this.authorReply = authorReply;
    }

    public Feedback(int feedbackId, int accountId, String accountName, String feedbackDate, String content, String rating, boolean active) {
        
        this.feedbackId = feedbackId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.feedbackDate = feedbackDate;
        this.content = content;
        this.rating = rating;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    

    public String getAuthorReply() {
        return authorReply;
    }

    public void setAuthorReply(String authorReply) {
        this.authorReply = authorReply;
    }

   

    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }
    
    

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
   

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    
    
}
