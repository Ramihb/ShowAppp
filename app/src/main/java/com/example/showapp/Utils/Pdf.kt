package com.example.showapp.Utils

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.showapp.Model.BillModelPDF
import com.example.showapp.Model.Facture
import com.example.showapp.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import java.io.*
import java.util.*


class Pdf(private val dataBill: BillModelPDF,private val facc: List<Facture>, private val mContext: Context) {
    lateinit var mSharedPref: SharedPreferences
    var ligneNumber: Int = 0

    fun savePDF() {

        var now         = Date().time
        val filename    = /*"$now"+*/"Bill.pdf"
        //val pathd        = File(mContext.getExternalFilesDir(null)!!.absolutePath + "/ShowappBills")
        val path     = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toURI())
        // create a folder
        val folder = File(path,"showapp files")
        if(!folder.exists())folder.mkdirs()
        //if (!path.exists())path.mkdirs()

        var file: File = File(folder, filename)

        if (file.exists())
        {
            file.delete()
            file = File(folder, filename)
        }

        val document = Document()

        PdfWriter.getInstance(document, FileOutputStream(file))

        document.open()
        document.pageSize = PageSize.A4
        document.addCreationDate()
        document.addAuthor("")
        document.addCreator("")

        val mColorAccent        = BaseColor(0, 153, 204, 255)
        val mHeadingFontSize    = 20.0f
        val mValueFontSize      = 26.0f



        val fontMontserrat =  BaseFont.createFont("res/font/fontmontserrat.ttf", "UTF-8", BaseFont.EMBEDDED)
        val lineSeparator  =  LineSeparator()

        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

        //adjust the size of the label
        val fontmontserratForHeader = Font(
            fontMontserrat,
            36.0f, Font.NORMAL,
            BaseColor.BLACK)

        val fontnormalcolorBlack = Font(
            fontMontserrat,
            mValueFontSize,
            Font.NORMAL,
            BaseColor.BLACK
        )
        val fontnormalcolorBlue = Font(
            fontMontserrat,
            mValueFontSize,
            Font.NORMAL,
            mColorAccent
        )
        val fontheading = Font(
            fontMontserrat,
            mHeadingFontSize,
            Font.NORMAL,
            BaseColor.BLACK
        )

        // for the image
        fun saveImageToInternalStorage(drawableId:Int): Uri {
            // Get the image from drawable resource as drawable object
            val drawable = ContextCompat.getDrawable(mContext,drawableId)

            // Get the bitmap from drawable object
            val bitmap = (drawable as BitmapDrawable).bitmap

            // Get the context wrapper instance
            val wrapper = ContextWrapper(mContext)

            // Initializing a new file
            // The bellow line return a directory in internal storage
            var file = wrapper.getDir("images", Context.MODE_PRIVATE)


            // Create a file to save the image
            file = File(file, "${UUID.randomUUID()}.jpg")

            try {
                // Get the file output stream
                val stream: OutputStream = FileOutputStream(file)

                // Compress bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                // Flush the stream
                stream.flush()

                // Close stream
                stream.close()
            } catch (e: IOException){ // Catch the exception
                e.printStackTrace()
            }

            // Return the saved image uri
            return Uri.parse(file.absolutePath)
        }
        val uri:Uri = saveImageToInternalStorage(R.drawable.showapp_pdf)
        //Log.i("waaa",uri.toString())

        //trying a table
        val imgFile = File(uri.toString())
        //Log.i("coucou",imgFile.exists().toString())
        var img: Image? = null
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val stream = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            img = Image.getInstance(byteArray)
        }
        //Log.i("waaa2",img.toString())
        //val Image = Image(img)
        document.add(img)


        //application Name
        //val paragraf1 = Paragraph(Chunk(dataBill.AppName,fontmontserratForHeader))
        //paragraf1.alignment = Element.ALIGN_LEFT
        //document.add(paragraf1)

        //seprator 1
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))


        val table = PdfPTable(3)
        table.paddingTop = 250f
        table.totalWidth = 100f
        table.widthPercentage = 100f

        val cell01 = PdfPCell(Phrase("Article Name"))
        cell01.horizontalAlignment = Element.ALIGN_CENTER
        val cell02 = PdfPCell(Phrase("Quantity"))
        cell02.horizontalAlignment = Element.ALIGN_CENTER
        val cell03 = PdfPCell(Phrase("Price"))
        cell03.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(cell01)
        table.addCell(cell02)
        table.addCell(cell03)
        table.completeRow()
        facc.forEach {
            var name = it.name.toString()
            Log.i("asboujaaa",name)
            var quantity = it.qte.toString()
            var price = it.price.toString() + " TND"
            ligneNumber = ligneNumber + 1
        val cell = PdfPCell(Phrase(name))
        val cell2 = PdfPCell(Phrase(quantity))
        val cell3 = PdfPCell(Phrase(price))
//        cell.setFixedHeight(13);
        //        cell.setFixedHeight(13);
        //cell.border = Rectangle.NO_BORDER
        //cell.colspan = 1
        //cell.backgroundColor = BaseColor.CYAN
        table.addCell(cell)
        table.addCell(cell2)
        table.addCell(cell3)
        table.completeRow()
        }

        document.add(table)

        //seprator 2
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        //total
        val  paragraf5          =   Paragraph("Total: ", fontnormalcolorBlack)
        paragraf5 .add(Chunk(VerticalPositionMark()))
        paragraf5 .add(dataBill.Total)
        document.add(paragraf5)

        //position

        val  paragraf6          =   Paragraph("Position: ", fontnormalcolorBlack)
        paragraf6 .add(Chunk(VerticalPositionMark()))
        //paragraf6 .add(dataBill.PositionAndCityName)
        document.add(paragraf6)
        val  paragraf7          =   Paragraph(dataBill.PositionAndCityName)
        paragraf7 .add(Chunk(VerticalPositionMark()))
        //paragraf7 .add(dataBill.PositionAndCityName)
        document.add(paragraf7)

        mSharedPref = mContext.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val pathhh = mSharedPref.getString("pathhh", null)
        Log.i("pathhh", pathhh.toString())
        if (pathhh!=null){
        var imgg: Image? = null

        val f = File(pathhh, "signature.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        val stream = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        imgg = Image.getInstance(byteArray)
            //imgg.setAbsolutePosition(350f,1f)
        document.add(imgg)
        }

        //seprator 2
        var number = "\n\n\n\n\n\n\n\n\n\n\n"
        if(ligneNumber<7){
        for(i in 1..ligneNumber-7) {
            number = number + "\n"
        }} else {
            number = ""
            for( i in 1..18-ligneNumber){
                number = number + "\n"
            }
        }
        //7
        //11
        val  paragraf8          =   Paragraph(number)
        paragraf8 .add(Chunk(VerticalPositionMark()))
        paragraf8.alignment = Element.ALIGN_BOTTOM
        document.add(paragraf8)
        document.add(Chunk(lineSeparator))
        val paragraf9 = Paragraph("© 2022 Hbaieb Rami & Mallek Ghassen, INC. All rights reserved")
        paragraf9.alignment = Element.ALIGN_BOTTOM
        document.add(paragraf9)
        //document.add(Paragraph("© 2022 Hbaieb Rami & Mallek Ghassen, INC. All rights reserved"))







        document.close()

    }
}

