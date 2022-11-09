package com.example.aa4rafaelyuki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Referência para o banco de dados
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();



    //Objetos gráficos do celular do garçom
    private EditText txtMesa;
    private EditText txtItem;
    private EditText txtProduto;
    private EditText txtPreco;
    private Button btnInserir;
    private Button btnListar;
    private Button btnCalcular;
    private Button btnZerar;

    //Objetos gráficos do celular da cozinha
    private EditText txtMesaCoz;
    private EditText txtItemCoz;
    private Button btnAtender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ligando os atributos com objetos da interface gráfica

        txtMesa = findViewById(R.id.txtMesa);
        txtItem = findViewById(R.id.txtItem);
        txtProduto = findViewById(R.id.txtProduto);
        txtPreco = findViewById(R.id.txtPreco);
        btnInserir = findViewById(R.id.btnInserir);
        btnListar = findViewById(R.id.btnListar);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnZerar = findViewById(R.id.btnZerar);


        txtMesaCoz = findViewById(R.id.txtMesaCoz);
        txtItemCoz = findViewById(R.id.txtItemCoz);
        btnAtender = findViewById(R.id.btnAtender);


        btnInserir.setOnClickListener(new EscutadorBotaoInserir());
        btnListar.setOnClickListener(new EscutadorBotaoListar());
        btnCalcular.setOnClickListener(new EscutadorBotaoCalcular());
        btnZerar.setOnClickListener(new EscutadorBotaoZerar());
        btnAtender.setOnClickListener(new EscutadorBotaoAtender());



    }

    public class EscutadorBotaoInserir implements View.OnClickListener{
        @Override

        public void onClick(View view){
            String produto, mesa, codigo;
            boolean atendido;
            double preco;

            DatabaseReference restaurante = BD.child( "restaurante" );

            produto = txtProduto.getText().toString();
            preco = Double.parseDouble(txtPreco.getText().toString());
            mesa = txtMesa.getText().toString();
            codigo = txtItem.getText().toString();


            Item i = new Item(produto, preco);
            restaurante.child(mesa).child(codigo).setValue(i);

            txtProduto.setText("");
            txtPreco.setText("");
            txtMesa.setText("");
            txtItem.setText("");





        }
    }

    private class EscutadorFirebase implements ValueEventListener {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String produto, mensagemAtendido;
            boolean atendido;
            double preco;

            if ( dataSnapshot.exists() ) {




                //Percorrendo o Snapshot e mostrando por meio de um toast

                for ( DataSnapshot datasnapItem : dataSnapshot.getChildren() ) {
                    Item i = datasnapItem.getValue( Item.class );

                    //Atribuindo os valores às variáveis
                    produto = i.getProduto();
                    atendido = i.isAtendido();
                    preco = i.getPreco();
                    if(atendido == true){
                        mensagemAtendido = "sim";
                    }else{
                        mensagemAtendido = "não";
                    }

                    //Exibindo no Toast
                    Toast.makeText(MainActivity.this, "Produto: "+produto+"\nAtendido: "+mensagemAtendido+"\nPreço: "+preco, Toast.LENGTH_SHORT).show();






                }
            }

        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    // Classe interna do escutador do botão LISTAR:

    private class EscutadorBotaoListar implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String mesa = txtMesa.getText().toString();
            DatabaseReference restaurante = BD.child("restaurante").child(mesa);

            txtMesa.setText("");


            restaurante.addValueEventListener( new EscutadorFirebase() );
        }
    }


    private class EscutadorFirebaseCalcula implements ValueEventListener {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            double preco, total = 0;

            if ( dataSnapshot.exists() ) {




                //Percorrendo o Snapshot e mostrando por meio de um toast

                for ( DataSnapshot datasnapItem : dataSnapshot.getChildren() ) {
                    Item i = datasnapItem.getValue( Item.class );

                    preco = i.getPreco();

                    total = total + preco;


                }


                Toast.makeText(MainActivity.this, "Total da conta: "+total, Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }




    private class EscutadorBotaoCalcular implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String mesa = txtMesa.getText().toString();
            DatabaseReference restaurante = BD.child("restaurante").child(mesa);

            txtMesa.setText("");


            restaurante.addValueEventListener( new EscutadorFirebaseCalcula() );
        }
    }


    private class EscutadorBotaoZerar implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String mesa = txtMesa.getText().toString();
            DatabaseReference restaurante = BD.child("restaurante");
            DatabaseReference m = restaurante.child( mesa );
            m.setValue(null);

            txtMesa.setText("");

        }
    }




    private class EscutadorFirebaseAtende implements ValueEventListener {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String mesa = txtMesaCoz.getText().toString();
            String codigo = txtItemCoz.getText().toString();



            boolean atendido;

            if ( dataSnapshot.exists() ) {


                    Item item = dataSnapshot.getValue(Item.class);
                    item.setAtendido(true);
                    DatabaseReference restaurante = BD.child("restaurante").child(mesa).child(codigo);
                    restaurante.setValue(item);

                //Toast.makeText(MainActivity.this, "Total da conta: "+total, Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }




    private class EscutadorBotaoAtender implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String mesa = txtMesaCoz.getText().toString();
            String codigo = txtItemCoz.getText().toString();
            DatabaseReference restaurante = BD.child("restaurante").child(mesa).child(codigo);

            txtMesa.setText("");


            restaurante.addValueEventListener( new EscutadorFirebaseAtende() );
        }
    }







}