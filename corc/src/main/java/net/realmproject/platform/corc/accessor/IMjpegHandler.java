package net.realmproject.platform.corc.accessor;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.impl.IDeviceAccessor;
import net.realmproject.dcm.event.bus.DeviceEventBus;
import net.realmproject.dcm.stock.camera.Frame;


public class IMjpegHandler extends IHandler<HttpRequest> {

    private static final byte[] CRLF = new byte[] { 0x0d, 0x0a };
    private static final String CONTENT_TYPE = "multipart/x-mixed-replace";
    private static final String DELIM = "delim";

    IDeviceAccessor<Frame> camera;

    public IMjpegHandler(String id, String deviceId, DeviceEventBus bus) {
        this.camera = new IDeviceAccessor<>(id, deviceId, bus);
    }

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    // Modified from
    // https://github.com/x-nagasawa/kmkt/blob/master/src/com/github/kmkt/util/MjpegServlet.java
    @Override
    protected void onExecute(Action action, HttpRequest request) throws IOException, ServletException {
        HttpServletRequest req = request.getHttpRequest();
        HttpServletResponse resp = request.getHttpResponse();
        OutputStream out = new BufferedOutputStream(resp.getOutputStream());

        BlockingQueue<byte[]> frames = new LinkedBlockingQueue<>(3);
        frames.offer(camera.getState().image);
        frames.offer(camera.getState().image);
        Consumer<Frame> listener = (frame -> {
            if (frame.image == null) { return; }
            try {
                frames.put(frame.image);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        camera.getListeners().add(listener);

        logger.info("Accept HTTP connection from " + req.getRemoteAddr());

        // write initial header
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(CONTENT_TYPE + ";boundary=" + DELIM);
        resp.setHeader("Connection", "keep-alive");

        // loop until we're interrupted or the connection is closed, waiting for
        // new frames and transmitting them
        try {
            while (true) {
                sendFrame(out, frames.take());
            }
        }
        catch (IOException e) {
            // connection closed
            logger.info("Close HTTP connection from " + req.getRemoteAddr());
        }
        catch (InterruptedException e) {
            // Interrupted
            out.close();
            e.printStackTrace();
        }
        finally {
            camera.getListeners().remove(listener);
        }
    }

    private void sendFrame(OutputStream out, byte[] frame) throws IOException {

        if (frame == null) return;

        byte[] content_type = "Content-Type: image/jpeg".getBytes();
        byte[] content_length = ("Content-Length: " + frame.length).getBytes();

        out.write(("--" + DELIM).getBytes());
        out.write(CRLF);
        out.write(content_type);
        out.write(CRLF);
        out.write(content_length);
        out.write(CRLF);
        out.write(CRLF);
        out.write(frame);
        out.write(CRLF);
        out.flush();

    }

}
