package il.ac.technion.cs.smarthouse.system;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/**
 * 
 * @author Inbal Zukerman
 * @date May 23, 2017
 */
public class DispatcherCore implements Dispatcher {

    private static final Map<String, Map<String, Consumer<String>>> subscribers = new HashMap<>();
    private static Logger log = LoggerFactory.getLogger(DispatcherCore.class);

    public static String getPathAsString(final String... pathNodes) {
        return String.join(Dispatcher.DELIMITER, pathNodes);
    }

    @Override
    public String subscribe(final Consumer<String> subscriber, final String... path) {
        if (!subscribers.containsKey(getPathAsString(path)))
            subscribers.put(getPathAsString(path), new HashMap<>());

        final String id = UuidGenerator.GenerateUniqueIDstring();

        subscribers.get(getPathAsString(path)).put(id, subscriber);
        return id;
    }

    @Override
    public void unsubscribe(final String subscriberId, final String... path) {
        if (!subscribers.containsKey(getPathAsString(path)))
            log.error("Key Word was not found");
        // TODO: inbal - shoud throw too?
        subscribers.get(getPathAsString(path)).remove(subscriberId);
    }

    @Override
    public void sendMessage(final InfoType infoType, final String value, final String... path) {
        // TODO: inbal - should get a message or path + value? maybe overload?
        final String message = infoType.toString() + DELIMITER + getPathAsString(path) + DELIMITER + value;
        for (final String prefix : subscribers.keySet())
            if (message.startsWith(prefix))
                subscribers.get(prefix).values().forEach(listener -> listener.accept(message));
    }

}
