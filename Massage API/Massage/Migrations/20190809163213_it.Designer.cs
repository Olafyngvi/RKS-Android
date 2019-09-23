﻿// <auto-generated />
using System;
using Massage.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

namespace Massage.Migrations
{
    [DbContext(typeof(MojContext))]
    [Migration("20190809163213_it")]
    partial class it
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "2.1.11-servicing-32099")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("Massage.Models.Korisnik", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("BrojTelefona");

                    b.Property<string>("Ime");

                    b.Property<string>("Lozinka");

                    b.HasKey("Id");

                    b.ToTable("korisnici");
                });

            modelBuilder.Entity("Massage.Models.Rezervacije", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("Dan");

                    b.Property<DateTime>("DatumRezervacije");

                    b.Property<int>("Godina");

                    b.Property<int>("Mjesec");

                    b.Property<int>("TerminId");

                    b.Property<long>("UserId");

                    b.HasKey("Id");

                    b.HasIndex("TerminId");

                    b.HasIndex("UserId");

                    b.ToTable("rezervacije");
                });

            modelBuilder.Entity("Massage.Models.Termini", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Termin");

                    b.Property<DateTime>("Vrijeme");

                    b.HasKey("Id");

                    b.ToTable("termini");
                });

            modelBuilder.Entity("Massage.Models.Rezervacije", b =>
                {
                    b.HasOne("Massage.Models.Termini", "Termin")
                        .WithMany()
                        .HasForeignKey("TerminId")
                        .OnDelete(DeleteBehavior.Cascade);

                    b.HasOne("Massage.Models.Korisnik", "User")
                        .WithMany()
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade);
                });
#pragma warning restore 612, 618
        }
    }
}
