package uk.kludje.proxy.test;

import uk.kludje.proxy.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import static uk.kludje.proxy.ProxyBinding.binder;
import static uk.kludje.proxy.ProxyBinding.proxy;

public class Click2 {
  private static final Interface<Consumer<MouseEvent>> CONSUMER = new Interface<Consumer<MouseEvent>>() {
  };

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setTitle(Click2.class.getSimpleName());
    frame.setLayout(new FlowLayout());
    frame.add(click(new JButton("Load"), Click2::load));
    frame.add(click(new JButton("Save"), Click2::save));
    frame.add(click(new JButton("Close"), Click2::close));
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private static <C extends JComponent> C click(C comp, Consumer<MouseEvent> listener) {
    MouseListener ml = binder(MouseListener.class, CONSUMER)
        .bind(proxy(MouseListener.class)::mouseClicked, listener::accept);
    comp.addMouseListener(ml);
    return comp;
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
