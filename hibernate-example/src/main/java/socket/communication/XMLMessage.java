package socket.communication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class XMLMessage {
		
	private MsgType type;
	private String desc;
	private List<?> sendable = new ArrayList<Object>();
	
	public MsgType getType() {
		return type;
	}
	public void setType(MsgType type) {
		this.type = type;
	}
	public List<?> getSendable() {
		return sendable;
	}
	public void setSendable(List<?> sendable) {
		this.sendable = sendable;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	

}
