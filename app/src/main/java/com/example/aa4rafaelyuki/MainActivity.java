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





        }
    }

    private class EscutadorFirebase implements ValueEventListener {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String produto;
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

                    //Exibindo no Toast
                    Toast.makeText(MainActivity.this, "Produto: "+produto+"\nAtendido: "+atendido+"\nPreço: "+preco, Toast.LENGTH_SHORT).show();






                }
            }

        }

        // Não usado...

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


            restaurante.addValueEventListener( new EscutadorFirebase() );
        }
    }


}