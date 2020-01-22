package io.pivotal.rsocketclient;


import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@ShellComponent
public class RSocketCommandSender {

    private final RSocketClient rSocketClient;

    @Autowired
    public RSocketCommandSender(RSocketClient rSocketClient) {
        this.rSocketClient = rSocketClient;
    }

    @ShellMethod("Send one request to the RSocket server. No response will be returned.")
    public void fireAndForget(@ShellOption(defaultValue = "fire-and-forget") String command) {
        log.info("\nSending fire and forget request. Expect no response (check server)...");
        rSocketClient.notifyCommand(command).subscribe().dispose();
        log.info("\nDone.");
        return;
    }

    @ShellMethod("Send one request to the RSocket server. One response will be printed.")
    public void requestResponse(@ShellOption(defaultValue = "request") String command) {
        log.info("\nSending one request. Waiting for one response...");
        rSocketClient.requestResponse(command).subscribe(cr -> log.info("\nEvent response is: {}", cr));
        return;
    }

    @ShellMethod("Send a stream of 10 requests to the RSocket server. The responses stream will be printed.")
    public void channel(@ShellOption(defaultValue = "channel") String command1){
        log.info("\nSending ten requests. Waiting for ten responses...");
        rSocketClient.channelCommand(command1).subscribe(er -> log.info("\nEvent Response is {}", er));
        return;
    }


    @ShellMethod("Send one request to the RSocket server. Many responses (stream) will be printed.")
    public void stream(@ShellOption(defaultValue = "stream") String command) {
        log.info("\nSending one request. Waiting for unlimited responses...");
        rSocketClient.streamCommand(command).subscribe(er -> log.info("\nEvent response is: {}", er));
        return;
    }
}
