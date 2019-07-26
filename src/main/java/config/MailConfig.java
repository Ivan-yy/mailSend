package config;

/**
 * 邮件发送配置
 */
public class MailConfig {
    //收件人地址
    private String recipientAddress;
    //抄送人地址，多个以","隔开
    private String copyToAddresses;
    //邮件标题
    private String subject;
    //邮件正文
    private String content;
    //附件路径
    private String[] attachmentPaths;

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getCopyToAddresses() {
        return copyToAddresses;
    }

    public void setCopyToAddresses(String copyToAddress) {
        this.copyToAddresses = copyToAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(String[] attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }
}
