(ns desafio01.desafio02-test
  (:require [clojure.test :refer :all]
            [desafio01.desafio02 :refer :all]
            [desafio01.model :as d.model]
            [schema.core :as s]))

(def cliente {:nome "Maria da Silva" :cpf "12356743565" :email "maria@gmal.com"
                :cartao {:numero  "1324567897643256" :cvv "123" :validade "03/2030" :limite 2000.00
                             :compras [{:data "01/11/2021" :valor 200.00 :estabelecimento "Extra" :categoria "Alimentação"}
                                       {:data "23/09/2020" :valor 545.00 :estabelecimento "Hospital Sao luiz" :categoria "Saúde"}
                                       {:data "03/04/2021" :valor 78.67 :estabelecimento "Extra" :categoria "Alimentação"}
                                       {:data "15/01/2020" :valor 21.30 :estabelecimento "Drograsil" :categoria "Saúde"}
                                       {:data "12/11/2021" :valor 567.99 :estabelecimento "Wise up" :categoria "Educação"}]}})

(def cliente-sem-compras {:nome "Maria da Silva" :cpf "12356743565" :email "maria@gmal.com"
                          :cartao {:numero  "1324567897643256" :cvv "123" :validade "03/2030" :limite 2000.00
                                   :compras []}})

(def compras-acima-de-duzentos [{:data "01/11/2021" :valor 200.00 :estabelecimento "Extra" :categoria "Alimentação"}
                                {:data "23/09/2020" :valor 545.00 :estabelecimento "Hospital Sao luiz" :categoria "Saúde"}
                                {:data "12/11/2021" :valor 567.99 :estabelecimento "Wise up" :categoria "Educação"}] )

(def compras-no-extra [{:data "01/11/2021" :valor 200.00 :estabelecimento "Extra" :categoria "Alimentação"}
                       {:data "03/04/2021" :valor 78.67 :estabelecimento "Extra" :categoria "Alimentação"}])

;TESTES PARA FUNCAO LISTAR COMPRAS

(deftest busca-compras-test

  (testing "Listar compras maior ou igual ao valor"
   (is (= compras-acima-de-duzentos
          (listar-compras cliente, 200))))

  (testing "Retornar vazio para valor inexistente"
    (is (= []
           (listar-compras cliente,600))))

  (testing "Listar compras por estabelecimento"
    (is (= compras-no-extra
           (listar-compras cliente, "Extra"))))

  (testing "Retornar vazio para estabelecimento inexistente"
    (is (= []
           (listar-compras cliente,"Pao de acucar"))))

  (testing "Retornar vazio para cliente sem compras"
    (is (=  []
           (listar-compras cliente-sem-compras, 0)))))