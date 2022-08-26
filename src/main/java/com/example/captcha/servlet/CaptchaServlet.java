package com.example.captcha.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@WebServlet("/captcha-servlet")
public class CaptchaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "No-cache");
        resp.setDateHeader("Expires", 0);
        resp.setHeader("Progma", "no-cache");
        resp.setDateHeader("Max-Age", 0);

        String captcha= generateCaptcha(4);

        int width = 160, height =35;
        BufferedImage bufferedImage= new BufferedImage(width, height, BufferedImage.OPAQUE);
        Graphics graphics =bufferedImage.createGraphics();
        graphics.setFont(new Font("Arial", Font.BOLD,20));
        graphics.setColor(new Color(169,169,169));
        graphics.fillRect(0,0, width, height);
        graphics.setColor(new Color(255,255,255));
        graphics.drawString(captcha, 20,25);

        HttpSession session= req.getSession(true);
        session.setAttribute("captcha", captcha);

        OutputStream outputStream = resp.getOutputStream();
        ImageIO.write(bufferedImage,"jpeg", outputStream);

    }

    private String generateCaptcha(int length) {
        String captcha = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuffer stringBuffer= new StringBuffer();
        Random random = new Random();

        while (stringBuffer.length() < length){
            int index = (int) (random.nextFloat()*captcha.length());
            stringBuffer.append(captcha.substring(index, index+1));
        }
        return stringBuffer.toString();
    }
}
