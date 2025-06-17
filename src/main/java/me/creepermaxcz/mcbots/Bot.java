package me.creepermaxcz.mcbots;

import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.auth.SessionService;
import org.geysermc.mcprotocollib.network.ClientSession;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.protocol.MinecraftConstants;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.data.UnexpectedEncryptionException;
import org.geysermc.mcprotocollib.protocol.data.game.ClientCommand;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerCombatKillPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundClientCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level.ServerboundAcceptTeleportationPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.*;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bot extends Thread {

    private String nickname;
    private ProxyInfo proxy;
    private InetSocketAddress address;
    private ClientSession client;
    private boolean hasMainListener;
    private ScheduledExecutorService executor;
    private int reconnectAttempts = 0;
    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private static final int RECONNECT_DELAY = 1000; // 1 second

    private double lastX, lastY, lastZ = -1;
    private float lastYaw = 0, lastPitch = 0;
    private boolean connected;
    private boolean manualDisconnecting = false;
    private boolean isAttacking = false;
    private long lastActionTime = 0;
    private static final long ACTION_COOLDOWN = 20; // Reduced to 20ms for faster actions
    private boolean isSneaking = false;
    private int movementPattern = 0;

    public Bot(MinecraftProtocol protocol, InetSocketAddress address, ProxyInfo proxy) {
        this.nickname = protocol.getProfile().getName();
        this.address = address;
        this.proxy = proxy;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        Log.info("Creating bot", nickname);

        client = ClientNetworkSessionFactory.factory()
                .setAddress(address.getHostString(), address.getPort())
                .setProtocol(protocol)
                .setProxy(proxy)
                .create();

        SessionService sessionService = new SessionService();
        client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
    }

    @Override
    public void run() {
        if (!Main.isMinimal()) {
            client.addListener(new SessionAdapter() {
                @Override
                public void packetReceived(Session session, Packet packet) {
                    if (packet instanceof ClientboundLoginPacket) {
                        connected = true;
                        Log.info(nickname + " connected");

                        // Start enhanced movement
                        startEnhancedMovement();

                        if (Main.joinMessages.size() > 0) {
                            for (String msg : Main.joinMessages) {
                                sendChat(msg);
                                try {
                                    Thread.sleep(10); // Reduced delay between messages
                                } catch (InterruptedException ignored) {}
                            }
                        }
                    }
                    else if (packet instanceof ClientboundPlayerPositionPacket) {
                        ClientboundPlayerPositionPacket p = (ClientboundPlayerPositionPacket) packet;
                        lastX = p.getPosition().getX();
                        lastY = p.getPosition().getY();
                        lastZ = p.getPosition().getZ();

                        client.send(new ServerboundAcceptTeleportationPacket(p.getId()));
                    }
                    else if (packet instanceof ClientboundPlayerCombatKillPacket) {
                        if (Main.autoRespawnDelay >= 0) {
                            Log.info("Bot " + nickname + " died. Respawning in " + Main.autoRespawnDelay + " ms.");
                            executor.schedule(() -> {
                                client.send(new ServerboundClientCommandPacket(ClientCommand.RESPAWN));
                                startEnhancedMovement();
                            }, Main.autoRespawnDelay, TimeUnit.MILLISECONDS);
                        }
                    }
                }

                @Override
                public void disconnected(DisconnectedEvent event) {
                    connected = false;
                    Log.info(nickname + " disconnected");
                    Main.removeBot(Bot.this);
                    Thread.currentThread().interrupt();
                }
            });
        }
        client.connect();
    }

    private void startEnhancedMovement() {
        // Start aggressive movement patterns
        executor.scheduleAtFixedRate(() -> {
            if (connected) {
                // Enhanced movement patterns
                switch (movementPattern) {
                    case 0: // Forward and back
                        move(0, 0, 2);
                        break;
                    case 1: // Side to side
                        move(2, 0, 0);
                        break;
                    case 2: // Diagonal
                        move(1.5, 0, 1.5);
                        break;
                    case 3: // Random direction
                        move((Math.random() - 0.5) * 4, 0, (Math.random() - 0.5) * 4);
                        break;
                }

                // Jump randomly
                if (Math.random() < 0.3) {
                    move(0, 1.0, 0);
                }

                // Change movement pattern randomly
                if (Math.random() < 0.2) {
                    movementPattern = (movementPattern + 1) % 4;
                }

                // Random looking
                look((float) (Math.random() * 360), (float) (Math.random() * 180 - 90));
            }
        }, 0, 20, TimeUnit.MILLISECONDS); // Increased frequency to 20ms
    }

    public void sendChat(String text) {
        if (text.startsWith("/")) {
            client.send(new ServerboundChatCommandPacket(text.substring(1)));
        } else {
            client.send(new ServerboundChatPacket(
                    text,
                    Instant.now().toEpochMilli(),
                    0L,
                    null,
                    0,
                    new BitSet()
            ));
        }
    }

    public void look(float yaw, float pitch) {
        lastYaw = yaw;
        lastPitch = pitch;
        client.send(new ServerboundMovePlayerPosRotPacket(true, true, lastX, lastY, lastZ, yaw, pitch));
    }

    public void move(double x, double y, double z) {
        lastX += x;
        lastY += y;
        lastZ += z;
        moveTo(lastX, lastY, lastZ);
    }

    public void moveTo(double x, double y, double z) {
        client.send(new ServerboundMovePlayerPosPacket(true, true, x, y, z));
    }

    public String getNickname() {
        return nickname;
    }

    public void registerMainListener() {
        hasMainListener = true;
        if (Main.isMinimal()) return;
        client.addListener(new MainListener(nickname));
    }

    public boolean hasMainListener() {
        return hasMainListener;
    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnect() {
        manualDisconnecting = true;
        executor.shutdown();
        client.disconnect("Leaving");
    }
}
