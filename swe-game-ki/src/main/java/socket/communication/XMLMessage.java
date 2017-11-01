package socket.communication;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class XMLMessage {
	
	public static String msgEnd = "</xmlMessage>";
	@XmlElement(required=true)
	private MsgType type;
	private String desc;
	private Object sendable;
	
	public MsgType getType() {
		return type;
	}
	public void setType(MsgType type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Object getSendable() {
		return sendable;
	}
	public void setSendable(Object sendable) {
		this.sendable = sendable;
	}

	
	
	
	
	

}
