package smallaswater.luckdraw.commands.items;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;
import smallaswater.luckdraw.utils.CustomItem;
import smallaswater.luckdraw.utils.Tools;

public class SaveItemSubCommand extends SubCommand {


    public SaveItemSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"保存"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(args.length > 1){
                String name = args[1];
                if("".equals(LuckDraw.getNbtItem(name))){
                    Item hand = ((Player) sender).getInventory().getItemInHand();
                    if(hand.getId() != 0){
                        String text = CustomItem.toStringItem(hand);
                        LuckDraw.getInstance().saveNbt(name,text);
                        sender.sendMessage("§a 成功将手持物品保存在 nbtitems.yml 名称:"+name);
                    }else{
                        sender.sendMessage("§c请不要保存空气");
                        return true;
                    }

                }else{
                    sender.sendMessage("§c"+name+"已存在..");
                }
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/lch save <名称> §7将手持物品保存在 nbtitem.yml文件";
    }
}
