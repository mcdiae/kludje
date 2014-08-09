package uk.kludje.experimental.test.proxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Click1 {
  public static void main(String[] args) {
    JButton loader = new JButton("Load");
    JButton saver = new JButton("Save");
    JButton closer = new JButton("Close");

    JFrame frame = new JFrame();
    frame.setTitle(Click1.class.getSimpleName());
    frame.setLayout(new FlowLayout());
    frame.add(loader);
    frame.add(saver);
    frame.add(closer);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    loader.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        load(e);
      }
    });
    saver.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        save(e);
      }
    });
    closer.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        close(e);
      }
    });
  }

  private static void load(MouseEvent event) {
    System.out.println("load");
  }

  private static void save(MouseEvent event) {
    System.out.println("save");
  }

  private static void close(MouseEvent event) {
    System.out.println("close");
  }
}
