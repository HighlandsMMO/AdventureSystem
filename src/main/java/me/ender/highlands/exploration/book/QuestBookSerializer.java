package me.ender.highlands.exploration.book;

import com.google.gson.*;
import me.ender.highlands.exploration.conditions.CitizensUnlock;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import me.ender.highlands.exploration.conditions.LocationUnlock;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class QuestBookSerializer implements JsonSerializer<QuestBook>, JsonDeserializer<QuestBook> {
    @Override
    public JsonElement serialize(QuestBook src, Type typeOfSrc, JsonSerializationContext context) {
        return serializeQuestBook(src);
    }
    @Override
    public QuestBook deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return deserializeQuestBook(json);
    }

    public static JsonObject serializeQuestBook(QuestBook src) {
        var book = new JsonObject();
        book.addProperty("title", src.title);
        book.addProperty("author", src.author);
        var jsonPages = new JsonArray();

        var pages = new JsonArray();
        for(var page : src.pages) {
            var jsonPage = new JsonObject(); // page
            var jsonComps = new JsonArray();

            for(var comp : page.getRawPage()) {
                jsonComps.add(serializeQuestComponent(comp));
            }

            jsonPage.add("components", jsonComps);

            if(page.getCondition()!= null) {
                jsonPage.add("conditions", serializeCondition(page.getCondition()));
            }

            jsonPages.add(jsonPage);
        }
        book.add("pages", jsonPages);

        return book;
    }
    public static QuestBook deserializeQuestBook(JsonElement json) {
        var jsonBook = json.getAsJsonObject();
        var title = jsonBook.get("title").getAsString();
        var author = jsonBook.get("author").getAsString();
        var book = new QuestBook(title, author);
        var contents = jsonBook.getAsJsonArray("pages");

        for(var p : contents) {
            var page = p.getAsJsonObject();
            var qpage = new QuestPage();
            //lines
            //region components
            for(var c : page.getAsJsonArray("components")) {
                var component = c.getAsJsonObject();
                var comp = deserializeQuestComponent(book,component);
                qpage.addComponent(comp);
            }

            //conditions

            var condition = page.getAsJsonObject("condition");
            if(condition != null) {
                qpage.setCondition(deserializeCondition(condition));
            }
            //conditions

            book.addPage(qpage);
        }
        return book;
    }

    private static JsonElement serializeQuestComponent(QuestComponent qc) {
        var jsonComp = serializeComponent(qc.getComponent());
        if(qc.getCondition() != null) {
            var condition = serializeCondition(qc.getCondition());
            jsonComp.add("condition", condition);
        }
        return jsonComp;
    }
    private static QuestComponent deserializeQuestComponent(QuestBook book, JsonObject component) {
        var qc = new QuestComponent();
        qc.setComponent(deserializeComponent(component));
        var condition =component.get("condition");
        if(condition != null) {
            qc.setCondition(deserializeCondition(condition.getAsJsonObject()));
        }

        return qc;
    }

    private static JsonObject serializeComponent(BaseComponent comp) {
        var jsonComp = new JsonObject();
        jsonComp.addProperty("text", comp.toPlainText());
        var color = comp.getColor();
        var bold = comp.isBold();
        var underline = comp.isUnderlined();
        var strike = comp.isStrikethrough();
        var italic = comp.isItalic();
        var obfuscated = comp.isObfuscated();

        var hoverEvent = comp.getHoverEvent();
        var clickEvent = comp.getClickEvent();

        if (color != ChatColor.BLACK)
            jsonComp.addProperty("color", color.getName());
        if (bold)
            jsonComp.addProperty("bold", true);
        if (underline)
            jsonComp.addProperty("underlined", true);
        if (strike)
            jsonComp.addProperty("strike", true);
        if (italic)
            jsonComp.addProperty("italic", true);
        if (obfuscated)
            jsonComp.addProperty("obfuscated", true);

        if (hoverEvent != null) {

            jsonComp.add("hoverEvent", serializeHoverEvent(hoverEvent));
        }
        if (clickEvent != null) {
            var jsonClick = new JsonObject();
            jsonClick.addProperty("action", clickEvent.getAction().name());
            jsonClick.addProperty("value", clickEvent.getValue());
            jsonComp.add("clickEvent", jsonClick);
        }
        return jsonComp;
    }
    private static BaseComponent deserializeComponent(JsonObject component) {
        var text = component.get("text").getAsString();
//        if(text.contains("&")) { //can not use & sign in thing
//            var str = ChatColor.translateAlternateColorCodes('&', text);
//            component = serializeComponent(new TextComponent(str));
//            text = component.get("text").getAsString();
//        }
        ChatColor color = ChatColor.BLACK;
        var colorstr = component.get("color");
        if(colorstr != null)
            color = ChatColor.of(colorstr.getAsString());
        var bold = component.get("bold") != null;
        var strike = component.get("strike") != null;
        var italic = component.get("italic") != null;
        var underlined = component.get("underlined") != null;
        var obfuscated = component.get("obfuscated") != null;

        var comp = new ComponentBuilder(text).create()[0];
        comp.setColor(color);
        comp.setBold(bold);
        comp.setItalic(italic);
        comp.setStrikethrough(strike);
        comp.setUnderlined(underlined);
        comp.setObfuscated(obfuscated);
        var jsonClick = component.get("clickEvent");
        if(jsonClick != null) {
            var click = jsonClick.getAsJsonObject();
            comp.setClickEvent(new ClickEvent(
                    Enum.valueOf(ClickEvent.Action.class, click.get("action").getAsString()),
                    click.get("value").getAsString()));
        }
        var jsonHover = component.get("hoverEvent");
        if(jsonHover != null) {
            comp.setHoverEvent(deserializeHoverEvent(jsonHover.getAsJsonObject()));
        }
        return comp;
    }

    private static JsonObject serializeHoverEvent(HoverEvent event) {
        var jsonHover = new JsonObject();
        jsonHover.addProperty("action", event.getAction().name());
        //jsonHover.add("contents", context.serialize(hoverEvent.getContents()));
        var contents = new JsonArray();
        switch(event.getAction()) {

            case SHOW_TEXT -> {
                for(Content content : event.getContents()) {
                    var text = (Text)content;
                    var value = text.getValue();
                    QuestComponent comp = null;
                    //handle simple string
                    if(value instanceof String s) {
                        contents.add(serializeComponent(new TextComponent(s)));
                    }
                    else {
                        for(var c : (BaseComponent[])value) {
                            contents.add(serializeComponent(c));
                        }
                    }
                }
            }
            default -> {
                return jsonHover;
            }
        }
        jsonHover.add("contents", contents);
        return jsonHover;
    }
    private static HoverEvent deserializeHoverEvent(JsonObject event) {
        var action = HoverEvent.Action.valueOf(event.get("action").getAsString());
        var jsonContents = event.getAsJsonArray("contents");
        var contents = new ArrayList<Content>();
        switch(action) {
            //only support text for now
            case SHOW_TEXT: {
                var ary = new BaseComponent[jsonContents.size()];
                for(int i=0; i < jsonContents.size(); i++) {
                    ary[i] = deserializeComponent(jsonContents.get(i).getAsJsonObject());
                }
                contents.add(new Text(ary));
            }break;
        }
        return new HoverEvent(action, contents);
    }

    private static JsonElement serializeCondition(IUnlockCondition condition) {
        var jsonCondition = new JsonObject();
        jsonCondition.addProperty("implementation", condition.getImplementation());
        jsonCondition.addProperty("identifier", condition.getIdentifier());
        jsonCondition.addProperty("event", condition.getEvent().name());
        if(condition.getUUID() == null) {
            condition.setUUID(UUID.randomUUID()); //need a uuid
        }
        jsonCondition.addProperty("uuid", condition.getUUID().toString());
        if(condition.getQuestReward() != null) {
            jsonCondition.add("reward", serializeReward(condition.getQuestReward()));
        }
        return jsonCondition;
    }
    private static IUnlockCondition deserializeCondition(JsonObject component) {
        IUnlockCondition condition = null;
        var impl = component.get("implementation").getAsString();
        var eventName = component.get("event").getAsString();

        switch(impl) { //maybe impl should be an enum
            case "npc":
                condition = new CitizensUnlock();
                condition.setEvent(Enum.valueOf(CitizensUnlock.Events.class, eventName));
                break;
            case "location":
                condition = new LocationUnlock();
                condition.setEvent(LocationUnlock.Events.valueOf(eventName));
            default:
                //bad bad bad
                break;
        }
        var rewardStr = component.get("reward");
        if(rewardStr != null) {
            condition.setQuestReward(deserializeReward(rewardStr.getAsJsonObject()));
        }
        condition.setIdentifier(component.get("identifier").getAsString());
        var uuidStr = component.get("uuid");
        UUID uuid = null;
        if(uuidStr == null)
            uuid = UUID.randomUUID();
        else
            uuid = UUID.fromString(uuidStr.getAsString());
        condition.setUUID(uuid);

        return condition;
    }

    private static JsonObject serializeReward(IQuestReward reward) {
        var json = new JsonObject();
        json.addProperty("implementation", reward.getClass().getSimpleName());
        json.addProperty("type", reward.getRewardType().name());
        json.addProperty("data", reward.getData());
        return json;
    }
    private static IQuestReward deserializeReward(JsonObject json) {
        var impl = json.get("implementation").getAsString();
        var type = IQuestReward.RewardType.valueOf(json.get("type").getAsString());
        var id = json.get("data").getAsString();
        //IQuestReward reward = null;
        switch(impl) {
            case "MMOQuestReward" -> {
                return new MMOQuestReward(id);
            }
        }
        return null;
    }
}
