package com.tomato.monitor.alarm.service;


import com.tomato.monitor.alarm.bean.AlarmBean;
import com.tomato.monitor.alarm.bean.ReceiveEmail;
import com.tomato.monitor.alarm.utils.ConfigUtil;
import com.tomato.monitor.alarm.utils.Constants;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: wangrun
 * Date: 2016/8/17 17:52
 */
public class EmailServiceImpl extends AlarmService {

    private static Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    private HtmlEmail email;

    public EmailServiceImpl() {
        email = new HtmlEmail();
        email.setHostName(ConfigUtil.getAllConfig().getString(Constants.EMAIL_HOST_NAME));
        email.setSmtpPort(ConfigUtil.getAllConfig().getInt(Constants.EMAIL_HOST_PORT));
        email.setAuthenticator(new DefaultAuthenticator(ConfigUtil.getAllConfig().getString(Constants.EMAIL_AUTHEN_USER),
                ConfigUtil.getAllConfig().getString(Constants.EMAIL_AUTHEN_PASSWORD)));
        email.setSSLOnConnect(true);
        email.setCharset("UTF-8");
        email.setDebug(false);
    }

    @Override
    public String send(AlarmBean alarmBean) throws Exception {
        ReceiveEmail receiveEmail = alarmBean.getReceiveEmail();
        email.setFrom(ConfigUtil.getAllConfig().getString(Constants.EMAIL_AUTHEN_USER), "邮件报警服务" , "UTF-8");
        email.addTo(receiveEmail.getRecipients());
        String ccs = receiveEmail.getCc();
        if (ccs != null && !"".equals(ccs.trim())) {
            email.addCc(ccs.split(","));
        }

        String subject = alarmBean.getLevel() + "级报警：" + alarmBean.getServiceName();
        email.setSubject(subject);
        //email.setMsg(content);
        email.setHtmlMsg(this.createHtml(alarmBean));
        email.setTextMsg("您的邮件不支持接收HTML信息。");
        String message = email.send();
        LOG.info("MSG: {}", message);
        return null;
    }

    private String createHtml(AlarmBean alarmBean) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body><br>");
        html.append("<strong>报警时间：</strong>");
        html.append(DateFormatUtils.format(alarmBean.getTimestamp(), "yyyy-MM-dd HH:mm:ss"));
        html.append("<br>");
        html.append("<strong>服务器IP：</strong>");
        html.append(alarmBean.getIp());
        html.append("<br>");
        html.append("<strong>报警内容：</strong>");
        html.append(alarmBean.getContent());
        html.append("<br><br><br><br>");
        html.append("该邮件为系统自动发送，请勿回复。");
        html.append("</body></html>");
        return html.toString();
    }

}
