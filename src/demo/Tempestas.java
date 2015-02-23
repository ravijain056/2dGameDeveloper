package demo;

import data.shapes.Rectangle2D;
import data.universe2D.RectangularSolid2D;
import data.universe2D.Renderer;
import data.universe2D.SolidObject2D;
import data.universe2D.Universe2D;
import data.universe2D.event.Object2DAdapter;
import data.universe2D.event.Object2DEvent;
import data.util.creature.RectangularUser;
import data.util.environment.Block;
import data.util.planet.RectangularEarth;
import data.vector2D.Direction2D;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Tempestas {

    int curFrame;
    GraphicsConfiguration gc;
    JFrame mainFrame;
    String sqlPassword, curProfile;
    Rectangle2D newGame, loadGame, exitGame;
    Image startscreen, brightback, darkback;
    boolean bstartscreen, autosave;

    {
        gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
        sqlPassword = "rauncreators";
        bstartscreen = true;
        autosave = true;

        startscreen = null;
        brightback = null;
        darkback = null;
        try {
            startscreen = ImageIO.read(getClass().getResource("/images/startscreen.jpg"));
            brightback = ImageIO.read(getClass().getResource("/images/brightback.jpg"));
            darkback = ImageIO.read(getClass().getResource("/images/darkback.jpg"));
        } catch (IOException ex) {
        }

        newGame = new Rectangle2D(gc.getBounds().getWidth() / 1600 * 835,
                gc.getBounds().getHeight() / 900 * 230, gc.getBounds().getWidth()
                / 1600 * 490, gc.getBounds().getHeight() / 900 * 75);
        loadGame = new Rectangle2D(gc.getBounds().getWidth() / 1600 * 890,
                gc.getBounds().getHeight() / 900 * 375, gc.getBounds().getWidth()
                / 1600 * 490, gc.getBounds().getHeight() / 900 * 75);
        exitGame = new Rectangle2D(gc.getBounds().getWidth() / 1600 * 930,
                gc.getBounds().getHeight() / 900 * 520, gc.getBounds().getWidth()
                / 1600 * 490, gc.getBounds().getHeight() / 900 * 75);

        mainFrame = new JFrame() {
            @Override
            public void paint(Graphics g) {
                for (Component comp : getComponents()) {
                    comp.paint(g);
                }
                if (bstartscreen) {
                    g.drawImage(startscreen, getX(), getY(), getWidth(),
                            getHeight(), this);
                }
            }
        };
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setUndecorated(true);
        mainFrame.setBounds(gc.getBounds());
        mainFrame.setLayout(null);

        mainFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int frame = 0;
                if (newGame.contains(e.getPoint())) {
                    do {
                        String name = "";
                            try (Connection Conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tempestas", "root",
                                    sqlPassword)) {
                                name = JOptionPane.showInputDialog("Please enter the name for your file.\n"
                                        + "NOTE: These name can be used later to "
                                        + "load your progress.");
                                if (name == null) {
                                    return;
                                }
                                if ("".equals(name)) {
                                    throw new IllegalArgumentException("Profile"
                                            + " name cannot be empty.");
                                }
                                Statement stmt = Conn.createStatement();
                                stmt.executeUpdate("INSERT INTO SavedGames"
                                        + " VALUES(\"" + name + "\", 1)");
                                curProfile = name;
                                bstartscreen = false;
                                break;
                            } catch (SQLException ex) {
                                if (ex.getErrorCode() == 1049) {
                                    int cont = JOptionPane.showConfirmDialog(null, "It seems that the game is not installed properly. "
                                            + "\nIts autosave feature is not functioning correctly. "
                                            + "\nStill the game can be played keeping this feature off."
                                            + "\n Would you like to continue turning autosave feature off? "
                                            + "\nNOTE: You would not be able to load you progress later.", "Error!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                    if (cont == JOptionPane.YES_OPTION) {
                                        autosave = false;
                                        break;
                                    } else {
                                        return;
                                    }
                                }
                                if (ex.getErrorCode() == 1045) {
                                    int cont = JOptionPane.showConfirmDialog(null,
                                            "The program doesnt contain the password to"
                                            + " have a mysql connection on this computer. "
                                            + "\nWould You like to provide it? \nNOTE: "
                                            + "You cannot load or save game without"
                                            + " providing mysql password.",
                                            "NO CONNECTION",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.WARNING_MESSAGE);
                                    if (cont == JOptionPane.YES_OPTION) {
                                        sqlPassword = JOptionPane.showInputDialog("Enter the Password of your "
                                                + "Mysql connection.");
                                    } else {
                                        autosave = false;
                                        break;
                                    }
                                } else if (ex.getErrorCode() == 1062) {
                                    int cont = JOptionPane.showConfirmDialog(null,
                                            "The profile with the given name "
                                            + "already exists.\n "
                                            + "Would you like to overwrite it?",
                                            "Duplicate profile.",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.WARNING_MESSAGE);
                                    if (cont == JOptionPane.YES_OPTION) {
                                        try (Connection Conn = DriverManager.
                                                getConnection("jdbc:mysql://localhost:3306/tempestas",
                                                "root", sqlPassword)) {
                                            Statement stmt = Conn.createStatement();
                                            stmt.executeUpdate("UPDATE SavedGames SET Frame"
                                                    + " = 1 WHERE Name = \"" + name + "\"");
                                            curProfile = name;
                                            bstartscreen = false;
                                            break;
                                        } catch (SQLException ex1) {
                                            System.out.println("ku");
                                            System.exit(1);
                                        }
                                    }
                                }
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(null, "Please enter a"
                                        + " valid profile name.", "Invalid input",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                    } while (true);
                    frame = 1;
                    loadGame(frame);
                } else if (loadGame.contains(e.getPoint())) {
                    do {
                        try (Connection Conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tempestas", "root",
                                sqlPassword);
                                Statement stmt = Conn.createStatement()) {
                            String name = JOptionPane.showInputDialog("Please "
                                    + "enter the name of your profile.");
                            if (name == null) {
                                return;
                            }
                            ResultSet res = stmt.executeQuery("SELECT frame "
                                    + "FROM SavedGames where name=\"" + name + "\"");
                            if (res.first()) {
                                frame = res.getInt("frame");
                                curProfile = name;
                                bstartscreen = false;
                                break;
                            } else {
                                throw new IllegalArgumentException("No such "
                                        + "saved profile :" + name);
                            }
                        } catch (SQLException ex) {
                            if (ex.getErrorCode() == 1049) {
                                JOptionPane.showMessageDialog(null, "It seems that the game is not installed properly. "
                                        + "\nCannot locate saved files.", "Error!", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (ex.getErrorCode() == 1045) {
                                int cont = JOptionPane.showConfirmDialog(null,
                                        "The program doesnt contain the pa"
                                        + "ssword to have a mysql connecti"
                                        + "on on this computer. "
                                        + "\nWould You like to provide it"
                                        + "? \nNOTE: You cannot load or s"
                                        + "ave game without providing mysql password.",
                                        "NO CONNECTION",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE);
                                if (cont == JOptionPane.YES_OPTION) {
                                    sqlPassword = JOptionPane.showInputDialog("Enter the Password of your "
                                            + "Mysql connection.");
                                } else {
                                    return;
                                }
                            }
                        } catch (IllegalArgumentException ex) {
                            int cont = JOptionPane.showConfirmDialog(null,
                                    "The queried profile doesnt exist.\n "
                                    + "Would you like to try to load another "
                                    + "profile?",
                                    "File Not Found!", JOptionPane.YES_NO_OPTION,
                                    JOptionPane.ERROR_MESSAGE);
                            if (cont == JOptionPane.NO_OPTION) {
                                return;
                            }
                        }
                    } while (true);
                    loadGame(frame);
                } else if (exitGame.contains(e.getPoint())) {
                    if (JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to quit?",
                            "Exit", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == 0) {
                        System.exit(0);
                    }
                }
                mainFrame.repaint();
            }
        });
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
        }

        mainFrame.setVisible(true);
    }

    public void loadGame(int frame) {
        Image defaultImage = null, land = null, ice = null, rock1 = null,
                rock2 = null, rock3 = null, rock4 = null,
                userleft[] = new Image[8], userright[] = new Image[8],
                box = null, platform = null;
        try {
            defaultImage = ImageIO.read(getClass().getResource("/images/image.jpg"));
            land = ImageIO.read(getClass().getResource("/images/land.jpg"));
            ice = ImageIO.read(getClass().getResource("/images/ice.jpg"));
            rock1 = ImageIO.read(getClass().getResource("/images/rock1.gif"));
            rock2 = ImageIO.read(getClass().getResource("/images/rock2.gif"));
            rock3 = ImageIO.read(getClass().getResource("/images/rock3.jpg"));
            rock4 = ImageIO.read(getClass().getResource("/images/rock4.jpg"));
            box = ImageIO.read(getClass().getResource("/images/box.jpg"));
            platform = ImageIO.read(getClass().getResource("/images/platform.gif"));
            for (int i = 0; i < 8; i++) {
                userleft[i] = ImageIO.read(getClass().getResource("/images/l" + (i + 1) + ".gif"));
                userright[i] = ImageIO.read(getClass().getResource("/images/r" + (i + 1) + ".gif"));
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

        curFrame = 1;
        final Universe2D uni = new Universe2D();

        final Renderer renderer = new Renderer(new Rectangle2D(0, 0, 32, 18));
        renderer.setUniverse(uni);
        renderer.setBounds(gc.getBounds());
        renderer.setScale(new Dimension((int) (gc.getBounds().getWidth() * 50
                / 1600), (int) (gc.getBounds().getHeight() * 50 / 900)));
        mainFrame.add(renderer);

        RectangularEarth earth = new RectangularEarth(uni);
        earth.setRange(new Rectangle2D(0, 0, 192, 18));
        earth.setBounds(new Rectangle2D(0, 26, 192, 0.1));
        earth.setImage(defaultImage);

        final Block blocker_l = new Block(uni);
        blocker_l.setBounds(new Rectangle2D(-0.1, 0, 0.1, 16));
        blocker_l.setImage(defaultImage);

        final Block blocker_u = new Block(uni);
        blocker_u.setBounds(new Rectangle2D(0, -0.1, 32, 0.1));
        blocker_u.setImage(defaultImage);

        Block land_1 = new Block(uni);
        land_1.setBounds(new Rectangle2D(0, 16, 32, 3));
        land_1.setImage(land);

        Block b1_1 = new Block(uni);
        b1_1.setBounds(new Rectangle2D(14, 13, 3, 0.28));
        b1_1.setImage(rock1);

        Block b1_2 = new Block(uni);
        b1_2.setBounds(new Rectangle2D(18, 10, 3, 0.28));
        b1_2.setImage(rock1);

        Block b1_3 = new Block(uni);
        b1_3.setBounds(new Rectangle2D(22, 7, 3, 0.28));
        b1_3.setImage(rock1);

        Block b1_4 = new Block(uni);
        b1_4.setBounds(new Rectangle2D(26, 4, 1.5, 12));
        b1_4.setImage(rock2);

        Block land_2 = new Block(uni);
        land_2.setBounds(new Rectangle2D(32, 16, 32, 3));
        land_2.setImage(land);

        Block b2_1 = new Block(uni);
        b2_1.setBounds(new Rectangle2D(32, 11, 25, 2));
        b2_1.setImage(rock4);

        Block b2_2 = new Block(uni);
        b2_2.setBounds(new Rectangle2D(38, 6, 25, 2));
        b2_2.setImage(rock4);

        Block b2_3 = new Block(uni);
        b2_3.setBounds(new Rectangle2D(32, 0, 2, 11));
        b2_3.setImage(rock3);

        Block b2_4 = new Block(uni);
        b2_4.setBounds(new Rectangle2D(61, 8, 2, 8));
        b2_4.setImage(rock3);

        Block b2_5 = new Block(uni);
        b2_5.setBounds(new Rectangle2D(34, 0, 29.9, 2));
        b2_5.setImage(rock4);

        Block ice3_1 = new Block(uni);
        ice3_1.setBounds(new Rectangle2D(64, 16, 11, 3));
        ice3_1.setImage(ice);
        ice3_1.setCoFriction(0);
        ice3_1.setCoFriction(-0.3);


        Block ice34_2 = new Block(uni);
        ice34_2.setBounds(new Rectangle2D(86, 16, 17, 3));
        ice34_2.setImage(ice);
        ice34_2.setCoFriction(-0.3);

        Block b4_2 = new Block(uni);
        b4_2.setBounds(new Rectangle2D(115, 6, 5, 0.28));
        b4_2.setImage(platform);
        b4_2.getVelocity().setComponentMagnitude(new Direction2D(225), 5);
        b4_2.addObject2DListener(new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                if (e.getSource().getBounds().getY() >= 15 || e.getSource().
                        getBounds().getY() <= 6) {
                    ((SolidObject2D) e.getSource()).getVelocity().reverse();
                }
            }
        });

        Block b4_3 = new Block(uni);
        b4_3.setBounds(new Rectangle2D(120, 6, 7.9, 2));
        b4_3.setImage(land);

        Block b5_1 = new Block(uni);
        b5_1.setBounds(new Rectangle2D(128, 10, 7, 2));
        b5_1.setImage(land);

        Block b5_2 = new Block(uni);
        b5_2.setBounds(new Rectangle2D(137, 7, 4, 0.28));
        b5_2.setImage(platform);
        b5_2.getVelocity().setComponentMagnitude(new Direction2D(90), 3);
        b5_2.addObject2DListener(new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                if (e.getSource().getBounds().getY() >= 14 || e.getSource().
                        getBounds().getY() <= 6) {
                    ((SolidObject2D) e.getSource()).getVelocity().reverse();
                }
            }
        });

        Block b5_3 = new Block(uni);
        b5_3.setBounds(new Rectangle2D(143, 10, 4, 0.28));
        b5_3.setImage(platform);
        b5_3.getVelocity().setComponentMagnitude(new Direction2D(90), 6);
        b5_3.addObject2DListener(new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                if (e.getSource().getBounds().getY() >= 14 || e.getSource().
                        getBounds().getY() <= 6) {
                    ((SolidObject2D) e.getSource()).getVelocity().reverse();
                }
            }
        });

        Block b5_4 = new Block(uni);
        b5_4.setBounds(new Rectangle2D(149, 13, 4, 0.28));
        b5_4.setImage(platform);
        b5_4.getVelocity().setComponentMagnitude(new Direction2D(90), 10);
        b5_4.addObject2DListener(new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                if (e.getSource().getBounds().getY() >= 14 || e.getSource().
                        getBounds().getY() <= 6) {
                    ((SolidObject2D) e.getSource()).getVelocity().reverse();
                }
            }
        });

        Block b5_5 = new Block(uni);
        b5_5.setBounds(new Rectangle2D(155, 16, 5, 2));
        b5_5.setImage(land);

        Block b6_1 = new Block(uni);
        b6_1.setBounds(new Rectangle2D(160, 16, 32, 3));
        b6_1.setImage(land);

        Block b6_2 = new Block(uni);
        b6_2.setBounds(new Rectangle2D(189, 0, 3, 16));
        b6_2.setImage(rock3);

        Block b6_3 = new Block(uni);
        b6_3.setBounds(new Rectangle2D(160, 11, 25, 2));
        b6_3.setImage(rock4);

        Block b6_4 = new Block(uni);
        b6_4.setBounds(new Rectangle2D(167, 5, 22, 2));
        b6_4.setImage(rock4);

        Block b6_5 = new Block(uni);
        b6_5.setBounds(new Rectangle2D(160, 0, 29, 2));
        b6_5.setImage(rock4);

        Block b6_6 = new Block(uni);
        b6_6.setBounds(new Rectangle2D(160, 2, 2, 9));
        b6_6.setImage(rock3);

        final Block b6_7 = new Block(uni);
        b6_7.setBounds(new Rectangle2D(162, 5, 5, 0.28));
        b6_7.setImage(platform);
        b6_7.addObject2DListener(new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                if (e.getSource().getBounds().getY() < 2.1) {
                    ((SolidObject2D) e.getSource()).getVelocity().setMagnitude(0);
                }
            }
        });

        RectangularSolid2D key = new RectangularSolid2D(uni) {
            @Override
            public double getStrength(Direction2D direc) {
                return Double.POSITIVE_INFINITY;
            }

            @Override
            public void harm(double harmFactor) {
            }
        };
        key.setBounds(new Rectangle2D(165, 10, 1, 1));
        key.setCoFriction(0);
        key.setMass(1);
        key.setImage(box);
        key.addObject2DListener(new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                if (e.getSource().getBounds().getX() == 162
                        && !(b6_7.getBounds().getY() < 2.1)) {
                    b6_7.getVelocity().setYComponent(-2);
                }
            }
        });

        RectangularUser user = new RectangularUser(uni, mainFrame);
        user.setBounds(new Rectangle2D(3, 0, 1, 1.5));
        user.setMass(50);
        user.setStrength(Double.POSITIVE_INFINITY);
        user.setImageArray(userleft, userright);
        user.addObject2DListener(new Object2DAdapter() {
            @Override
            public void boundsChanged(Object2DEvent e) {
                if (e.getSource().getBounds().getX()
                        > renderer.getViewPort().getX() + 32) {
                    renderer.setViewPort(new Rectangle2D(renderer.getViewPort().getX() + 32, 0, 32, 18));
                    blocker_l.setBounds(new Rectangle2D(renderer.getViewPort().getX() - 0.1, 0, 0.1, 16));
                    blocker_u.setBounds(new Rectangle2D(renderer.getViewPort().getX(), -0.1, 32, 0.1));
                    curFrame += 1;
                    if (autosave) {
                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tempestas", "root",
                                sqlPassword);
                                Statement stmt = conn.createStatement()) {
                            stmt.executeUpdate("UPDATE SavedGames SET Frame = "
                                    + curFrame + " WHERE Name"
                                    + " = \"" + curProfile + "\"");
                        } catch (SQLException ex) {
                        }
                    }
                    if (curFrame == 2 || curFrame == 6) {
                        renderer.setBackgroundImg(darkback);
                    } else if (curFrame == 1 || curFrame == 3
                            || curFrame == 4 || curFrame == 5) {
                        renderer.setBackgroundImg(brightback);
                    }
                }
                if (e.getSource().getBounds().getY() > 20) {
                    e.getSource().setBounds(new Rectangle2D(renderer.getViewPort().getX() + 2, 7,
                            e.getSource().getBounds().getWidth(),
                            e.getSource().getBounds().getHeight()));
                    ((SolidObject2D) e.getSource()).getVelocity().setMagnitude(0);
                }
                if (e.getSource().getBounds().getX() >= 186 && e.getSource().
                        getBounds().getY() <= 5) {
                    uni.pause();
                    JOptionPane.showMessageDialog(null, "The demo version of "
                            + "the game is finished.\n"
                            + "The full version of the game will be released "
                            + "soon and can be downloaded from www.rauncreators.com.");
                    System.exit(0);
                }
            }
        });
        if (frame == 1 || frame == 2 || frame == 3 || frame == 4 || frame == 6) {
            user.setBounds(new Rectangle2D(32 * (frame - 1) + 2, 14, 1, 1.5));
        } else if (frame == 5) {
            user.setBounds(new Rectangle2D(32 * (frame - 1) + 2, 18, 1, 1.5));
        }
        if (frame == 2 || frame == 6) {
            renderer.setBackgroundImg(darkback);
        } else if (frame == 1 || frame == 3 || frame == 4 || frame == 5) {
            renderer.setBackgroundImg(brightback);
        }
        uni.play();
    }

    public static void main(String[] args) {
        new Tempestas();
    }
}
