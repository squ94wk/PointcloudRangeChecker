import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Checker {
  private final static List<Double> xs = new LinkedList<>();
  private final static List<Double> ys = new LinkedList<>();

  private final static List<Rectangle> rectangles = new LinkedList<>();
  private final static List<String> lasFiles = new LinkedList<>();

  private final static File output = new File("src/output");
  private static Writer writer;

  static {
    try {
      writer = new BufferedWriter(new FileWriter(output));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    Rectangle bbox = null;

    //import Rectangles
    if (args.length > 0) {
      // File file = new File(args[0]);
      if (args.length == 5) {
        double[] bounds = new double[4];
        for (int i = 1; i < 5; i++) {
          bounds[i - 1] = Double.parseDouble(args[i]);
        }
        bbox = new Rectangle(bounds[0], bounds[1], bounds[2], bounds[3]);
      }
      File file = new File("res/SanFran.csv");
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String read;
      try {
        while ((read = reader.readLine()) != null) {
          Matcher matcher = Pattern.compile("-?\\d+\\.\\d+").matcher(read);
          double[] c = new double[6];
          for (int i = 0; i < 6; i++) {
            if (matcher.find()) {
              final String group = matcher.group();
              double coordinate = Double.parseDouble(group);
              c[i] = coordinate;
            } else {
              throw new IllegalArgumentException();
            }
          }

          try {
            Rectangle rectangle = new Rectangle(c[0], c[2], c[1], c[3], c[5]);
            if (bbox != null && !bbox.intersect(rectangle)) {
              continue;
            }
            lasFiles.add(read.substring(2, 20) + ".las");
            importRectangle(rectangle);
          } catch (IllegalArgumentException ignored) {
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      importRectangle(new Rectangle(-122.411540241183388, 37.7703554, -122.409505641882447, 37.7780973));

      importRectangle(new Rectangle(-122.410765441449598, 37.6080368, -122.393747347296767, 37.6216418));
      importRectangle(new Rectangle(-122.410760641451247, 37.7650371, -122.409567341861248,
          37.7703598));
      importRectangle(new Rectangle(-122.410743441457157, 37.6215561, -122.393637747334424,
          37.6351608));
      importRectangle(new Rectangle(-122.410636841493783, 37.6350757, -122.393527347372356,
          37.6486796));
      importRectangle(new Rectangle(-122.410529441530684, 37.6485951, -122.393418847409635,
          37.6621583));
      importRectangle(new Rectangle(-122.410175341652348, 37.6891537, -122.393088147523258,
          37.7027573));
      importRectangle(new Rectangle(-122.410100941677911, 37.7026727, -122.392978047561087,
          37.7162219));
      importRectangle(new Rectangle(-122.409607841847333, 37.7567498, -122.39253584771302,
          37.7703543));
      importRectangle(new Rectangle(-122.409565041862038, 37.7702685, -122.392425247751021,
          37.7838476));
      importRectangle(new Rectangle(-122.409310241949584, 37.7432298, -122.392646247675088,
          37.7568252));
      importRectangle(new Rectangle(-122.407681842509078, 37.6756338, -122.39319754748567,
          37.6892251));
    }

    //sort
    Comparator<Double> sort = (x1, x2) -> {
      if (x1 > x2) {
        return 1;
      }
      if (x1 < x2) {
        return -1;
      }
      return 0;
    };
    xs.sort(sort);
    ys.sort(sort);

    final boolean[][] contains = new boolean[xs.size() - 1][ys.size() - 1];

    System.out.println(lasFiles.stream().collect(Collectors.joining("\r\n")));

    for (int j = ys.size() - 2; j >= 0; j--) {
      for (int i = 0; i < xs.size() - 1; i++) {
        contains[i][j] = false;
        Rectangle r = new Rectangle(xs.get(i), ys.get(j), xs.get(i + 1), ys.get(j + 1));
        char tile = ' ';
        for (final Rectangle rectangle : rectangles) {
          if (rectangle.contains(r)) {
            final double height = rectangle.getHeight();
            contains[i][j] = true;
            if (height > 2000) {
              tile = '█';
            } else if (height > 1600) {
              tile = '▓';
            } else if (height > 600) {
              tile = '▒';
            } else {
              tile = '░';
            }
            break;
          }
        }

        write(tile);
      }
      write("\r\n");
    }

    if (writer != null) {
      writer.close();
    }
  }

  private static void importRectangle(final Rectangle rectangle) {
    rectangles.add(rectangle);

    double x1 = rectangle.getX1();
    double x2 = rectangle.getX2();
    double y1 = rectangle.getY2();
    double y2 = rectangle.getY2();

    if (!xs.contains(x1)) {
      xs.add(x1);
    }
    if (!xs.contains(x2)) {
      xs.add(x2);
    }
    if (!ys.contains(y1)) {
      ys.add(y1);
    }
    if (!ys.contains(y2)) {
      ys.add(y2);
    }
  }

  private static void write(final String tile) throws IOException {
    if (writer != null) {
      writer.write(tile);
    } else {
      System.out.print(tile);
    }
  }

  private static void write(final char tile) throws IOException {
    if (writer != null) {
      writer.write(tile);
      writer.flush();
    } else {
      System.out.println(tile);
    }
  }
}
