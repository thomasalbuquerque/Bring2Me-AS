package com.example.echo.bring2me.BD_e_Controle;
//branchAVALIACAO
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://echob2m.esy.es/bd_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://echob2m.esy.es/bd_api/register.php";

    // Server user order url
    public static String URL_ORDER = "http://echob2m.esy.es/bd_api/pedido.php";

    // Server user register trip url
    public static String URL_VIAGENS = "http://echob2m.esy.es/bd_api/CadastraViagem.php";

    // Server user search trip url
    public static String URL_BUSCAVIAGENS = "http://echob2m.esy.es/bd_api/buscaviagens.php";

    public static String URL_BUSCAVIAGENSCadastradas = "http://echob2m.esy.es/bd_api/buscaViagemByUser.php";

    public static String URL_REMOVEVIAGEMCadastrada = "http://echob2m.esy.es/bd_api/deletaViagem.php";

    public static String URL_IMAGEM = "http://echob2m.esy.es/verviagem.png";

    public static int AvaliacaoPadraoDoViajante = 5;

    public static String URL_PEDIDOSRecebidos = "http://echob2m.esy.es/bd_api/buscaPedidos.php";

    public static String URL_PEDIDOSFeitos = "http://echob2m.esy.es/bd_api/buscaPedidoFeito.php";

    public static String URL_AVALIAPedido = "http://echob2m.esy.es/bd_api/avaliado.php";
}