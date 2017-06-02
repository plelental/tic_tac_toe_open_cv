package tictactoe.service.paint;

import tictactoe.model.Cell;
import tictactoe.model.Mark;
import tictactoe.service.ai.AIService;
import tictactoe.service.coordinates.CellCoordinate;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;


/**
 * @author Lelental on 05.05.2017.
 */
public class Nought implements ICanBePainted {

    private Point pointOfCircle;
    private List<Cell> cells;
    private Mat mat;

    Nought(List<Cell> cells, Mat mat) {
        this.cells = cells;
        this.mat = mat;
    }

    @Override
    public void paint()  {

        if (pointOfCircle != null) {

            int noughtThickness = 7;
            double x = pointOfCircle.x;
            double y = pointOfCircle.y;
            int radius = 50;
            Scalar noughtColor = new Scalar(0,10,255);

            for (Cell cell : cells) {

                if (cell.isPainted() && cell.getMark() != Mark.Cross) {
                    Imgproc.circle(mat, cell.getCenterPoint(), radius, noughtColor, noughtThickness);
                }

                if (x > cell.getMinX() && x < cell.getMaxX() && y > cell.getMinY() && y < cell.getMaxY()
                        && cell.getMark() == null) {

                    cell.setPainted(true);
                    cell.setMark(Mark.Nought);
                    CellCoordinate.setMarkBoard(cell.getRow(), cell.getColumn(), cell.getMark());
                    AIService.turn++;

                }
            }
        }
    }

    void setPointOfCircle(Point pointOfCircle) {
        this.pointOfCircle = pointOfCircle;
    }
}