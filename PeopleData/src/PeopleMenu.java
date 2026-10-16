import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeopleMenu extends JFrame {
    // index baris yang diklik
    private int selectedIndex = -1;

    // connect dengan db untuk menampung semua mahasiswa
    private Database database;


    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunField;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JRadioButton yaRadioButton;
    private JRadioButton tidakRadioButton;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JPanel mainPanel;
    private JTable personTable;

    public static void main(String[] args) {
        // buat object window
        PeopleMenu menu = new PeopleMenu();

        // atur ukuran window
        menu.setSize(600, 500);

        // Letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // constructor
    public PeopleMenu() {
        // inisialisasi database
        database = new Database();

        // isi tabel mahasiswa
        personTable.setModel(setTable());

        // buat button group untuk radio "mhs aktif"
        ButtonGroup yesNoGroup = new ButtonGroup();
        yesNoGroup.add(yaRadioButton);
        yesNoGroup.add(tidakRadioButton);
        yaRadioButton.setSelected(true);

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = { "???", "Ilmu Komputer", "Musik", "Akuntansi", "Sastra Inggris", "Arsitektur", "Fisika" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin menghapus data mahasiswa?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        personTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = personTable.getSelectedRow();

                // value textfield dan combo box (all input)
                String curId = personTable.getModel().getValueAt(selectedIndex, 0).toString();
                String curNama = personTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curTahun = personTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curKategori = personTable.getModel().getValueAt(selectedIndex, 3).toString();

                // disable idfield karena id tidak bisa diedit di db
                idField.setEnabled(false);

                // ubah isi textfield dan combo box
                idField.setText(curId);
                namaField.setText(curNama);
                tahunField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                if (personTable.getModel().getValueAt(selectedIndex, 4).toString().equals("Ya")) {
                    yaRadioButton.setSelected(true);
                    tidakRadioButton.setSelected(false);
                } else {
                    yaRadioButton.setSelected(false);
                    tidakRadioButton.setSelected(true);
                }

                // ubah button 'add' jadi 'update'
                addUpdateButton.setText("update");

                // tampilkan button delete
                deleteButton.setVisible(true);

            }
        });

    }

    private boolean idTaken(String id) {
        try {
            ResultSet rs = database.selectQuery("SELECT id FROM person WHERE id = '" + id + "'");
            if (rs != null && rs.next()) {
                return true; // ID sudah ada di database
            }
        } catch (SQLException e) {
            System.err.println("Error checking ID: " + e.getMessage());
        }
        return false;
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "ID (NIM)", "Nama", "Tahun Lahir", "Kategori (Prodi)", "Aktif" };

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM person");

            // isi tabel dengan hasil query
            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("nama");
                row[2] = resultSet.getString("thn_lahir");
                row[3] = resultSet.getString("kategori");
                row[4] = (resultSet.getBoolean("aktif") ? "Ya" : "Tidak");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tmp;

    }

    public void insertData() {
        // ambil value textfield dan combobox
        String id = idField.getText();
        String nama = namaField.getText();
        String tahun = tahunField.getText();
        String kategori = kategoriComboBox.getSelectedItem().toString();
        boolean aktif = yaRadioButton.isSelected();

        // validasi kolom kosong
        if (id.isEmpty() || nama.isEmpty() || tahun.isEmpty() || kategori.equals("???")) {
            JOptionPane.showMessageDialog(null, "Semua kolom input wajib diisi!", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // validasi id
        if (idTaken(id)) {
            JOptionPane.showMessageDialog(null, "ID '" + id + "' sudah terdaftar dalam database!", "Error ID Duplikat", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tahunInt = Integer.parseInt(tahun);

            // add data ke db
            String sqlQuery = "INSERT INTO person (id, nama, thn_lahir, kategori, aktif) VALUES ('"
                    + id + "', '" + nama + "', " + tahunInt + ", '" + kategori + "', " + aktif + ")";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            personTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        // ambil data form
        String id = idField.getText();
        String nama = namaField.getText();
        String tahun = tahunField.getText();
        String kategori = kategoriComboBox.getSelectedItem().toString();
        boolean aktif = yaRadioButton.isSelected();

        // validasi kolom kosong
        if (id.isEmpty() || nama.isEmpty() || tahun.isEmpty() || kategori.equals("???")) {
            JOptionPane.showMessageDialog(null, "Semua kolom input wajib diisi!", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tahunInt = Integer.parseInt(tahun);

            // ubah data mahasiswa di db
            String sqlQuery = "UPDATE person SET id = '" + id + "', nama = '" + nama + "', thn_lahir = " + tahunInt + ", kategori = '"
                    + kategori + "', aktif = " + aktif + " WHERE id = '" + id + "'";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            personTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        // ubah data mahasiswa di db
        String id = personTable.getModel().getValueAt(selectedIndex, 0).toString();
        String sqlQuery = "DELETE FROM person WHERE id = '" + id + "'";
        database.insertUpdateDeleteQuery(sqlQuery);

        // update tabel
        personTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        idField.setText("");
        namaField.setText("");
        tahunField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        yaRadioButton.setSelected(true);
        tidakRadioButton.setSelected(false);

        // ubah button "update" menjadi "add"
        addUpdateButton.setText("add");

        // aktifkan field id
        idField.setEnabled(true);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}