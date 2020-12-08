package smallaswater.luckdraw.items;


import smallaswater.luckdraw.utils.Tools;


/**
 * @author 若水
 */
public class ItemMessage {

    private String msg;

    private String broad;


    public ItemMessage(String msg,String broad){
        this.msg = msg;
        this.broad = broad;
    }


    public void setBroad(String broad) {
        this.broad = broad;
    }

    public String getBroad() {
        return broad;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public static ItemMessage toMessage(String str){
        String r = Tools.getSubUtilSimple(str,"<msg>(.*?)</msg>");
        String r1 = Tools.getSubUtilSimple(str,"<broad>(.*?)</broad>");
        String msg = "";
        String broad = "";
        if(!"".equals(r)){
            msg = r;
        }
        if(!"".equals(r1)){
            broad = r1;
        }
        if(!"".equals(msg) || !"".equals(broad)){
            return new ItemMessage(msg,broad);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(!"".equals(msg)){
            builder.append("<msg>");
            builder.append(msg);
            builder.append("</msg>");
        }
        if(!"".equals(broad)){
            builder.append("<broad>");
            builder.append(broad);
            builder.append("</broad>");
        }
        return builder.toString();
    }
}
