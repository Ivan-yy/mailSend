package send;


import config.MailConfig;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送类
 */
public class MailUtil {
    private static Logger logger = Logger.getLogger(MailUtil.class);

    public static void sendErrorMail(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        MailConfig mc = new MailConfig();
        mc.setSubject(e.getMessage());
        mc.setContent(sw.toString());
        mc.setRecipientAddress("707300693@qq.com");
        sendMail(mc);
    }

    /**
     * SMTP服务器配置及邮件发送
     * @throws Exception
     */
    public static void sendMail(MailConfig mc) {
        try{
            // 连接邮件服务器的参数配置
            Properties props = new Properties();
            // 设置用户的认证方式
            props.setProperty("mail.smtp.auth", "true");
            // 设置传输协议(JavaMail规范要求)
            props.setProperty("mail.transport.protocol","smtp");
            // 设置发件人的SMTP服务器地址
            props.setProperty("mail.smtp.host", "smtp.163.com");
            //  设置SMTP服务器端口 一般填写：25
            props.setProperty("mail.smtp.port","25");

            // 创建定义整个应用程序所需的环境信息的 Session 对象
            Session session = Session.getInstance(props);
            // 设置调试信息在控制台打印出来
            session.setDebug(true);
            // 创建邮件的实例对象
            Message msg = getMimeMessage(session, mc);
            // 根据session对象获取邮件传输对象Transport
            Transport transport = session.getTransport();

            transport.connect("18851969519@163.com", "yy123456");
            // 发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg,msg.getAllRecipients());
            // 关闭邮件连接
            transport.close();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * 获取邮件的实例对象
     * @param session
     * @return MimeMessage
     * @throws MessagingException
     * @throws AddressException
     */
    private static MimeMessage getMimeMessage(Session session, MailConfig mc) throws Exception{
        // 创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);

        // 设置发件人地址
        msg.setFrom(new InternetAddress("18851969519@163.com"));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        if (mc.getRecipientAddress()!=null ){
            InternetAddress[] recipients = new InternetAddress().parse(mc.getRecipientAddress());
            msg.setRecipients(MimeMessage.RecipientType.TO, recipients);
        }else{
            throw new Exception("收件人地址为空");
        }
        if (mc.getCopyToAddresses()!=null ){
            InternetAddress[] copyTos = new InternetAddress().parse(mc.getCopyToAddresses());
            msg.setRecipients(MimeMessage.RecipientType.CC,copyTos);
        }
        // 设置邮件主题
        if (mc.getSubject()!=null ){
            msg.setSubject(mc.getSubject(),"UTF-8");
        }else{
            throw new Exception("邮件主题为空");
        }
        //设置邮件正文
        // 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
        MimeMultipart mm = new MimeMultipart();
        // 混合关系
        mm.setSubType("mixed");
        // 创建附件"节点"
        MimeBodyPart body = new MimeBodyPart();
        if (mc.getContent()!=null ){
            body.setContent(mc.getContent(), "text/html;charset=UTF-8");
        }else{
            throw new Exception("邮件内容为空");
        }
        mm.addBodyPart(body);     // 如果有多个附件，可以创建多个多次添加
        if(mc.getAttachmentPaths()!=null && mc.getAttachmentPaths().length>0){
            for (String attachmentPath: mc.getAttachmentPaths()) {
                // 创建附件"节点"
                MimeBodyPart attachment = new MimeBodyPart();
                // 读取本地文件
                DataHandler dh = new DataHandler(new FileDataSource(attachmentPath));
                // 将附件数据添加到"节点"
                attachment.setDataHandler(dh);
                // 设置附件的文件名（需要编码）
                attachment.setFileName(MimeUtility.encodeText(dh.getName()));
                mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
            }
        }
        // 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        msg.setContent(mm);
        // 设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        msg.saveChanges();
        return msg;
    }

    public static void main(String args[]){
        System.out.println(null=="");
        MailConfig mc = new MailConfig();
        mc.setSubject("测试邮件！");
        mc.setContent("内容不重要哦！");
        mc.setRecipientAddress("707300693@qq.com");
        //Exception e = new Exception("111");
        new MailUtil().sendMail(mc);
    }

}
