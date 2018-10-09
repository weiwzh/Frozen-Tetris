import java.util.Random;
import java.lang.Math;

/*
 * 
 * Properties of Shapes, rotations and flips
 * 
 * TYPES:
 * 0 - no shape
 * 1 - Z-
 * 2 - S-
 * 3 - L-
 * 4 - Reverse L-
 * 5 - Square 
 * 6 - Line 
 * 7 - T 
 * 
 * 
 */
public class Shape {
        private int type;
        private int coords[][];
        private int[][][] coordsTable = {     //coords of all shapes
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, 1 },   { 0, 0 },   { -1, 1},   { 1, 0 } },
                { { 0, -1},   { 0, 0 },   { -1, -1},  { 1, 0 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   {-1, -1} },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 1, -1} },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 2, 0 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } }
        };

        //constructor
        public Shape(int t) {
            coords = new int[4][2];
            type = t;        
        }

        //sets shape depending on its type
        public void setShape (int t) {
            type = t;
            for (int i = 0; i < 4 ; i++) {
                for (int j = 0; j < 2; ++j) {
                    coords[i][j] = coordsTable[type][i][j];
                }
            }
        }

        //generates random shape
        public void setRandomShape() {
            Random r = new Random();
            type = Math.abs(r.nextInt()) % 7 + 1; //+1 so don't generate empty
            setShape(type);
        }

        public int minY() {
            int m = coords[0][1];
            for (int i=0; i < 4; i++) {
                m = Math.min(m, coords[i][1]);
            }
            return m;
        }    
    
        public int getX(int index) {
            return coords[index][0];
        }

        public int getY(int index) {
            return coords[index][1];
        }

        public int getType() {
            return type;
        }

        //rotate clockwise
        public Shape rotate() {
            // if square, don't do anything
            if (type == 5) {
                return this;
            }
            Shape result = new Shape(0);
            result.type = type;

            for (int i = 0; i < 4; ++i) {
                result.coords[i][0] = -coords[i][1];
                result.coords[i][1] = coords[i][0];
            }
            return result;
        }
    
        //flip vertically
        public Shape flip() {
            // if square or line, don't do anything
            if (type == 5 || type == 6) {
                return this;
            }

            Shape result = new Shape(0);
            result.type = type;

            for (int i = 0; i < 4; ++i) {
                result.coords[i][0] = coords[i][0];
                result.coords[i][1] = -coords[i][1];
            }
            return result;
        }
    
}
