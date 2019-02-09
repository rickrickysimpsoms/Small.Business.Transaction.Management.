package com.example.developer.calvin.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.developer.calvin.R;

import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.PRINT_SERVICE;


public class ProfitFragment extends Fragment {
    EditText edCommodityName, edBuyingPrice,edProfit,edSellingPrice;
    Button bn;
    ProgressDialog pd;
    Snackbar snackbar;
    public static ProfitFragment newInstance() {
        ProfitFragment fragment = new ProfitFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.Calculator);
        View view = inflater.inflate(R.layout.fragment_profit, container, false);
        edCommodityName = view.findViewById(R.id.edCommodityName);
        edBuyingPrice = view.findViewById(R.id.edBuyingPrice);
        edProfit = view.findViewById(R.id.edProfit);
        edSellingPrice = view.findViewById(R.id.edSellingPrice);
        bn = view.findViewById(R.id.bn);
        pd = new ProgressDialog(getActivity());

        bn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                printPDF(view);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        };
        edBuyingPrice.addTextChangedListener(textWatcher);
        edProfit.addTextChangedListener(textWatcher);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printPDF(View view) {
        PrintManager printManager = (PrintManager) getActivity().getSystemService(PRINT_SERVICE);
        printManager.print("Report", new ViewPrintAdapter(getActivity(), view.findViewById(R.id.rv)), null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public class ViewPrintAdapter extends PrintDocumentAdapter {

        private PrintedPdfDocument mDocument;
        private Context mContext;
        private View mView;

        public ViewPrintAdapter(Context context, View view) {
            mContext = context;
            mView = view;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback, Bundle extras) {

            mDocument = new PrintedPdfDocument(mContext, newAttributes);

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(1);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                            CancellationSignal cancellationSignal,
                            WriteResultCallback callback) {


            PdfDocument.Page page = mDocument.startPage(0);

            Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            mView.draw(canvas);

            Rect src = new Rect(0, 0, mView.getWidth(), mView.getHeight());
            // get the page canvas and measure it.
            Canvas pageCanvas = page.getCanvas();
            float pageWidth = pageCanvas.getWidth();
            float pageHeight = pageCanvas.getHeight();

            float scale = Math.min(pageWidth/src.width(), pageHeight/src.height());
            float left = pageWidth / 2 - src.width() * scale / 2;
            float top = pageHeight / 2 - src.height() * scale / 2;
            float right = pageWidth / 2 + src.width() * scale / 2;
            float bottom = pageHeight / 2 + src.height() * scale / 2;
            RectF dst = new RectF(left, top, right, bottom);

            pageCanvas.drawBitmap(bitmap, src, dst, null);
            mDocument.finishPage(page);

            try {
                mDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                mDocument.close();
                mDocument = null;
            }
            callback.onWriteFinished(new PageRange[]{new PageRange(0, 0)});
        }
    }

    private void calculate() {
        double buyingprice = 0;
        double profit = 0.0;
        double sellingprice;

        if (edBuyingPrice != null)
            buyingprice = Double.parseDouble(!edBuyingPrice.getText().toString().equals("")? edBuyingPrice.getText().toString():"0");
        if (edProfit != null)
            profit = Double.parseDouble(!edProfit.getText().toString().equals("")? edProfit.getText().toString():"0");
        sellingprice = ((100 + profit) * buyingprice)/100;
        String textResult = String.valueOf(sellingprice);
        edSellingPrice.setText(textResult+"Ksh");
    }

    public void showSnackbar(String stringSnackbar){
        snackbar.make(getActivity().findViewById(android.R.id.content), stringSnackbar.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

}



