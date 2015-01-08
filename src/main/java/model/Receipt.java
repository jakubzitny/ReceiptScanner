package model;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by d_rc on 08/12/14.
 */
public class Receipt {

    private static final int CONTRAST_BORDER = 60;

    private BufferedImage mOriginalImage, mPreProcessedImage;
    private ArrayList<ArrayList<Integer>> mPixelMatrix = new ArrayList<ArrayList<Integer>>();
    private int mHeight;
    private int mWidth;

    private ArrayList<ArrayList<Integer>> mClusterMatrix = new ArrayList<ArrayList<Integer>>();
    private List<Cluster> mClusterList = new LinkedList<Cluster>();
    private int mClusters = 0;

    public Receipt() { }

    public Receipt(MultipartFile multipartFile) throws IOException {
        File file = multiPartFile2File(multipartFile);
        init(file);
    }

    public Receipt(File file) throws IOException {
        init(file);
    }

    public void init(File file) throws IOException {
        mOriginalImage = ImageIO.read(file);
        //mPreProcessedImage = ImagePreProcessor.brighten(ImagePreProcessor.desaturate(mOriginalImage));
        mHeight = mOriginalImage.getHeight();
        mWidth = mOriginalImage.getWidth();
        imageToPixelMatrix(mOriginalImage);
        //imageToPixelMatrix(mPreProcessedImage);
    }

    public BufferedImage separateLogo() {
        initClusterMatrix();
        searchForClusters();
        Collections.sort(mClusterList, Cluster.getComparator());
        int p = findLargeClusters();
        Cluster logo;
        if (p == 0) {
            logo = mClusterList.get(0);
        } else {
            //System.out.println("Merging " + p + " largest clusters.");
            logo = new Cluster(mClusterList.subList(0, p));
        }
        BufferedImage logoImage = mOriginalImage.getSubimage(logo.getLeft().y, logo.getTop().x,
                logo.getRight().y - logo.getLeft().y, logo.getBottom().x - logo.getTop().x);
        return logoImage;
    }

    private int findLargeClusters() {
        Cluster topCluster = mClusterList.get(0);
        int topClusterDigits = (int) (Math.log10(topCluster.getSize()) + 1);
        int p = 0; // potential to merge
        for (Cluster c: mClusterList) {
            int cDigits = (int) Math.log10(c.getSize()) + 1;
            if (cDigits == topClusterDigits) p++;
            //System.out.println("Cluster " + c.getNumber() + ": " + c.getSize());
        }
        return p;
    }

    private void searchForClusters() {
        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                if (mPixelMatrix.get(i).get(j) == 1 && mClusterMatrix.get(i).get(j) == 0) {
                    Cluster c = new Cluster(++mClusters);
                    mClusterList.add(c);
                    buildCluster(i, j, c);
                }
            }
        }
    }

    private void buildCluster(int i, int j, Cluster cluster) {
        mClusterMatrix.get(i).set(j, cluster.getNumber());
        cluster.add(i, j);
        int ii = i;
        int jj = j + 1;
        buildClusterNeighbor(ii, jj, cluster);
        ii = i + 1;
        jj = j + 1;
        buildClusterNeighbor(ii, jj, cluster);
        ii = i + 1;
        jj = j;
        buildClusterNeighbor(ii, jj, cluster);
        ii = i + 1;
        jj = j - 1;
        buildClusterNeighbor(ii, jj, cluster);
        ii = i;
        jj = j - 1;
        buildClusterNeighbor(ii, jj, cluster);
        ii = i - 1;
        jj = j - 1;
        buildClusterNeighbor(ii, jj, cluster);
        ii = i - 1;
        jj = j;
        buildClusterNeighbor(ii, jj, cluster);
        ii = i - 1;
        jj = j + 1;
        buildClusterNeighbor(ii, jj, cluster);
    }

    private void buildClusterNeighbor(int ii, int jj, Cluster cluster) {
        if (ii >= 0 && jj >= 0 && ii < mClusterMatrix.size() && jj < mClusterMatrix.get(0).size() &&
                mPixelMatrix.get(ii).get(jj) == 1 && mClusterMatrix.get(ii).get(jj) == 0) {
            buildCluster(ii, jj, cluster);
        }
    }

    private void imageToPixelMatrix(BufferedImage image) {
        for (int i = 0; i < image.getHeight(); i++) {
            ArrayList<Integer> line = new ArrayList<Integer>();
            for (int j = 0; j < image.getWidth(); j++) {
                int rgb = image.getRGB(j, i);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                int grayLevel = (r + g + b) / 3;
                // add contrast
                int pixel = 1;
                if (grayLevel > CONTRAST_BORDER) pixel = 0;
                //System.out.print(pixel);
                line.add(pixel);
            }
            //System.out.println();
            mPixelMatrix.add(line);
        }
    }

    private void initClusterMatrix() {
        for (int i = 0; i < mHeight; i++) {
            ArrayList<Integer> clusterLine = new ArrayList<Integer>();
            for (int j = 0; j < mWidth; j++) {
                clusterLine.add(0);
            }
            mClusterMatrix.add(clusterLine);
        }
    }

    private File multiPartFile2File(MultipartFile multipartFile) throws IOException {
        File convFile = new File(multipartFile.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public BufferedImage getOriginalImage() {
        return mOriginalImage;
    }


}
