import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @ClassName: Test
 * @Description: TODO
 * @Author: zwt
 * @Date: 2022/9/28 1:46
 */
public class GetGridsFromTwoPointsConnectedLine {
    private static class Pos {
        int X;
        int Y;

        public Pos(int x, int y) {
            X = x;
            Y = y;
        }

        @Override
        public String toString() {
            return "(" + X +
                    "," + Y +
                    ')';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos pos = (Pos) o;
            return X == pos.X && Y == pos.Y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(X, Y);
        }
    }

    public static void main(String[] args) {
        Pos from = new Pos(3, 0);
        Pos to = new Pos(0, 1);
        Set<Pos> points = GetTouchedPosBetweenTwoPoints(from, to);
        System.out.println(points);
    }

    /// <summary>
    /// 计算两点间经过的格子
    /// </summary>
    public static Set<Pos> GetTouchedPosBetweenTwoPoints(Pos from, Pos to)
    {
        Set<Pos> touchedGrids = GetTouchedPosBetweenOrigin2Target(new Pos(to.X - from.X, to.Y - from.Y));
        Set<Pos> touchedGridsTemp = new HashSet<>();
        for (Pos v : touchedGrids)
        {
            touchedGridsTemp.add(new Pos(v.X + from.X, v.Y + from.Y));
        }
        return touchedGridsTemp;
    }

    /// <summary>
    /// 计算目标位置到原点所经过的格子
    /// </summary>
    static Set<Pos> GetTouchedPosBetweenOrigin2Target(Pos target)
    {
        Set<Pos> touched = new HashSet<>();
        boolean steep = Math.abs(target.Y) > Math.abs(target.X);
        int x = steep ? target.Y : target.X;
        int y = steep ? target.X : target.Y;

        //斜率
        double tangent = (double) y / x;

        double delta = x > 0 ? 0.5 : -0.5;

        for (int i = 1; i < 2 * Math.abs(x); i++)
        {
            double tempX = i * delta;
            double tempY = tangent * tempX;
            boolean isOnEdge = Math.abs(tempY - Math.floor(tempY)) == 0.5;

            //偶数 格子内部判断
            if ((i & 1) == 0)
            {
                //在边缘,则上下两个格子都满足条件
                if (isOnEdge)
                {
                    touched.add(new Pos((int)Math.round(tempX), (int)Math.ceil(tempY)));
                    touched.add(new Pos((int)Math.round(tempX), (int)Math.floor(tempY)));
                } else  //不在边缘就所处格子满足条件
                {
                    touched.add(new Pos((int)Math.round(tempX), (int)Math.round(tempY)));
                }
            }

            //奇数 格子边缘判断
            else
            {
                //在格子交点处,不视为阻挡,忽略
                if (isOnEdge)
                {
                    continue;
                }
                //否则左右两个格子满足
                else
                {
                    touched.add(new Pos((int)Math.ceil(tempX), (int)Math.round(tempY)));
                    touched.add(new Pos((int)Math.floor(tempX), (int)Math.round(tempY)));
                }
            }
        }

        if (steep)
        {
            //镜像翻转 交换 X Y
            Set<Pos> touchedTemp = new HashSet<>();
            for (Pos v : touched)
            {
                Pos vTemp = new Pos(v.X, v.Y);
                vTemp.X = vTemp.X ^ vTemp.Y;
                vTemp.Y = vTemp.X ^ vTemp.Y;
                vTemp.X = vTemp.X ^ vTemp.Y;
                touchedTemp.add(vTemp);
            }
            touched.clear();
            touched.addAll(touchedTemp);
        }
        touched.remove(new Pos(0, 0));
        touched.remove(new Pos(target.X, target.Y));
        return touched;
    }
}
