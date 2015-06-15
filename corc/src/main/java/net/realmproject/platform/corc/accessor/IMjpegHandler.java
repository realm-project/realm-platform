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

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.dcm.accessor.impl.IDeviceAccessor;
import net.realmproject.dcm.device.stock.camera.Frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class IMjpegHandler extends IHandler<HttpRequest> {

    IDeviceAccessor<Frame> camera;

    public IMjpegHandler(IDeviceAccessor<Frame> camera) {
        this.camera = camera;
    }

    private static final byte[] CRLF = new byte[] { 0x0d, 0x0a };
    private static final String CONTENT_TYPE = "multipart/x-mixed-replace";

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    @Override
    protected void onExecute(Action action, HttpRequest request) throws IOException, ServletException {
        HttpServletRequest req = request.getHttpRequest();
        HttpServletResponse resp = request.getHttpResponse();

        BlockingQueue<byte[]> frames = new LinkedBlockingQueue<>(10);

        String remote = req.getRemoteAddr() + ":" + req.getRemotePort();

        Consumer<Frame> listener = (frame -> {
            frames.offer(frame.image);
        });

        try {

            logger.info("Accept HTTP connection from " + remote);

            String delemeter_str = Long.toHexString(System.currentTimeMillis());
            byte[] delimiter = ("--" + delemeter_str).getBytes();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(CONTENT_TYPE + ";boundary=" + delemeter_str);
            resp.setHeader("Connection", "Close");

            camera.getListeners().add(listener);
            OutputStream out = new BufferedOutputStream(resp.getOutputStream());
            try {
                while (true) {
                    sendFrame(out, frames.take(), delimiter);
                }
            }
            catch (IOException e) {
                // connection closed
                logger.info("Close HTTP connection from " + remote);
            }
            catch (InterruptedException e) {
                // Interrupted
                out.close();
                e.printStackTrace();
            }

        }
        finally {
            camera.getListeners().remove(listener);
        }

    }

    private void sendFrame(OutputStream out, byte[] frame, byte[] delimiter) throws IOException {
        if (frame == null) return;

        byte[] content_type = "Content-Type: image/jpeg".getBytes();
        byte[] content_length = ("Content-Length: " + frame.length).getBytes();

        out.write(delimiter);
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
